package tcp;

import bus.EBus;
import bus.Eventable;
import com.google.common.eventbus.Subscribe;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.System.arraycopy;

public class TCPClient extends Thread {

    private final static Logger log = LoggerFactory.getLogger(TCPClient.class);

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int    SERVER_PORT = 12900;

    private static Socket socket;
    private String host = SERVER_HOST;
    private int    port = SERVER_PORT;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public TCPClient() {
        this.host = SERVER_HOST;
        this.port = SERVER_PORT;
    }

    @Override public void run() {
        try {
            openSocket(new Socket(host, port));
            getSocket().setTcpNoDelay(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Connected to server on {}:{}", getSocket().getInetAddress(), getSocket().getPort());

        InputHandler inputHandler = new InputHandler(getSocket());
        inputHandler.start();

        OutputHandler outputHandler = new OutputHandler(getSocket());
        outputHandler.start();
    }

    private Socket getSocket() {
        return socket;
    }

    private void openSocket(Socket clientSocket) {
        socket = clientSocket;
    }

    private class OutputHandler extends Thread implements Eventable {
        private Socket               socket;
        private BufferedOutputStream bos;

        private OutputHandler(Socket socket) {
            this.socket = socket;
            try {
                bos = new BufferedOutputStream(this.socket.getOutputStream());
                registeredOnBus(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override public void run() {
        }

        @Subscribe private synchronized void sendData(String type) throws IOException {
            String json = "{type: " + type + "}";
            byte[] data = new byte[Integer.BYTES + json.length()];
            byte[] headerSize = Ints.toByteArray(json.length());

            arraycopy(headerSize, 0, data, 0, headerSize.length);
            arraycopy(json.getBytes(), 0, data, headerSize.length, json.getBytes().length);

            bos.write(data);
            bos.flush();
        }
    }

    private class InputHandler extends Thread {
        private Socket              innerSocket;
        private BufferedInputStream bis;
        private byte[] headerData = new byte[4];

        private InputHandler(Socket socket) {
            innerSocket = socket;
            try {
                bis = new BufferedInputStream(innerSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override public void run() {
            while (innerSocket.isConnected()) try {
                readData(bis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readData(BufferedInputStream bis) throws IOException {
            if (bis.available() == 0) return; // Проверяем наличие байт на чтение
            if (bis.read(headerData) != headerData.length) return; // Считываем первые 2 байта
            int sizeBuffer = Ints.fromByteArray(headerData); // Получаем размер пакета
            byte resultsBuffer[] = new byte[sizeBuffer]; // Буфер данных

            do {
                int r = bis.read(resultsBuffer); // количество прочитанных байт
                // Если количество прочитанных байт меньше, чем размер пакета, то продолжаем считывание
                if (r < sizeBuffer) continue;
                // Интерпретируем байты
                final String data = new String(resultsBuffer, 0, r);
                ChangePeopleEvent changePeople = new Gson().fromJson(data, ChangePeopleEvent.class);
                /*if (changePeople.getZid().equals("sz0")) EBus.post(new SafetyZoneEvent(changePeople.getNumOfPeople()));
                else */
                EBus.post(changePeople);

                System.out.println(changePeople.getZid() + ":::" + changePeople.getNumOfPeople());
                break;
            } while (true);
        }
    }
}
