package sample;

import bus.EBus;
import bus.Eventable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.extendetGeometry.BIMExt;
import json.extendetGeometry.BIMLoader;
import tcp.TCPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final Image imagePlay;
    private final Image imageStop;

    @FXML public Pane       canvas;
    @FXML public AnchorPane content;
    @FXML public Accordion  rightPanel;
    @FXML public ToolBar    toolBar;
    @FXML public Group      gRoot;
    @FXML public TextField  zoneIdField;
    @FXML public TextField  zoneNumOfPeopleField;
    @FXML public TextField  sensorIdField;
    @FXML public TextField  sensorDeviceIdField;
    @FXML public ChoiceBox  sensorTypeField;
    @FXML public TitledPane sensorTab;
    @FXML public TitledPane networkTab;
    @FXML public TextField  hostField;
    @FXML public TextField  portField;
    @FXML public TitledPane zoneTab;
    @FXML public TitledPane transitionTab;
    @FXML public TextField  transitionIdField;
    @FXML public TextField  transitionWidthField;
    @FXML public TextField  searchNodeField;
    @FXML public Button     searchNode;
    @FXML public Button     playSimulation;
    @FXML public Button     stopSimulation;
    @FXML public Button     disconnect;

    private TCPClient client;

    {
        System.setProperty("user.workspace", "/home/boris/workspace/java/jSimulationMoving/src/main/resources");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream isPlay = classloader.getResourceAsStream("play.png");
        InputStream isStop = classloader.getResourceAsStream("stop.png");
        imagePlay = new Image(isPlay);
        imageStop = new Image(isStop);
    }

    public void openFileHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.workspace"))); // or user.home
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        try {
            File file = fileChooser.showOpenDialog(new Stage());
            BIMExt bim = loadBim(file);
            FxBimHandler bimHandler = new FxBimHandler(bim, this);
            bimHandler.drawBim(gRoot);
        } catch (NullPointerException e) {
            System.out.println("File not selected");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private BIMExt loadBim(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        BIMLoader<BIMExt> bimLoader = new BIMLoader<>(fis, BIMExt.class);

        return bimLoader.getBim();
    }

    public void saveFileHandler() {
    }

    public void addSensorHandler() {
    }

    public void addLightHandler() {
    }

    public void addArrowHandler() {
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        canvas.toBack();
        canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            gRoot.setLayoutX(0);
            gRoot.prefWidth(newValue.doubleValue());
        });
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            gRoot.setLayoutY(newValue.doubleValue());
            gRoot.prefHeight(newValue.doubleValue());
        });

        playSimulation.setGraphic(new ImageView(imagePlay));
        stopSimulation.setGraphic(new ImageView(imageStop));
    }

    public void networkHandler() {
        networkTab.setVisible(true);
        networkTab.setExpanded(true);
    }

    public void connectHandler() {
        InetSocketAddress socketAddress;

        if (hostField.getText().isEmpty()) hostField.setText("localhost");
        if (!portField.getText().isEmpty())
            socketAddress = new InetSocketAddress(hostField.getText(), Integer.parseInt(portField.getText()));
        else return;

        client = new TCPClient(socketAddress.getHostName(), socketAddress.getPort());
        client.start();

        disconnect.setDisable(false);
        playSimulation.setVisible(true);
        stopSimulation.setVisible(true);
    }

    public void disconnectHandler() throws InterruptedException {
        if (client.isAlive()) client.interrupt();
        networkTab.setExpanded(false);
        networkTab.setVisible(false);
        disconnect.setDisable(true);
        playSimulation.setVisible(false);
        stopSimulation.setVisible(false);
    }

    public void playSimulationHandler() {
        EBus.post("start");
    }

    public void stopSimulationHandler() {
        EBus.post("stop");
    }
}
