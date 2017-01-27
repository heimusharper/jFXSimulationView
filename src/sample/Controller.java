package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.extendetGeometry.BIMExt;
import json.extendetGeometry.BIMLoader;
import json.extendetGeometry.ZoneExt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static json.geometry.Zone.FLOOR;

public class Controller implements Initializable {
    @FXML public Pane       canvas;
    @FXML public AnchorPane content;
    @FXML public Accordion  rightPanel;
    @FXML public ToolBar    toolBar;
    @FXML public Group      gRoot;
    DoubleProperty myScale = new SimpleDoubleProperty(0.5);
    private Stage primaryStage;

    {
        System.setProperty("user.workspace", "/home/boris/workspace/java/jSimulationMoving/src/main/resources");
    }

    public void openFileHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.workspace"))); // or user.home
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        try {
            File file = fileChooser.showOpenDialog(primaryStage);
            BIMExt bim = loadBim(file);
            drawBIM(bim);
        } catch (NullPointerException e) {
            System.out.println("File not selected");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void drawBIM(BIMExt bim) {
        ArrayList<ZoneExt> zones = new ArrayList<>(bim.getRooms().size());
        bim.getRooms().parallelStream().forEach(z -> z.getZones().parallelStream().forEach(zones::add));

        List<Double> levels = zones.parallelStream().filter(e -> e.getType() == FLOOR /*|| e.getType() == STAIRS*/)
                .map(e -> e.getXyz(0, 0, 2)).distinct().sorted().collect(Collectors.toList());

        double currentLevel = levels.get(0);
        int numOfLevels = levels.size();

        Group gZones = new Group();

        for (Iterator<ZoneExt> iter = bim.getZones().values().iterator(); iter.hasNext(); ) {
            ZoneExt zone = iter.next();
            if (zone.isSafetyZone()) {
                iter.remove();
                continue; // Отсекаем безопасную зону, она не имеет геометрических параметров
            }

            // Комнаты и лестницы отображаем по-разному
            switch (zone.getType()) {
            case FLOOR:
                if (!insidePoint(currentLevel, zone.getLevel())) continue;
                gRoot.getChildren().add(drawZone(bim, currentLevel, numOfLevels, 10, zone, iter));
                break;
            }
        }
    }

    private Polygon drawZone(BIMExt bim, double level, double s, double zoom, ZoneExt z, Iterator<ZoneExt> iter) {
        final Polygon p = new Polygon();

        for (int c = 0; c < z.getXyz().length; c++) {  // кольца
            for (int j = 0; j < z.getXyz()[0].length; j++) {  // точки
                double x = z.getXyz(c, j, 0) * zoom;
                double y = (s - z.getXyz(c, j, 1)) * zoom;
                p.getPoints().addAll(x, y);
                p.setId(z.getId());
            }
        }

        int valFill = (int) ((z.getNumOfPeople() * 255) / bim.getNumOfPeople()) * 10;
        p.setFill(Color.rgb(0, valFill > 255 ? 255 : valFill, 0));

        z.numberOfPeopleProperty().addListener(e -> {
            final double val = ((z.getNumberOfPeople() * 255) / bim.getNumOfPeople()) * 10;
            final int colorVal = (int) val > 255 ? 255 : (int) val;
            p.setFill(Color.rgb(0, colorVal, 0));
        });

        iter.remove();
        return p;
    }

    /**
     * Метод проверки вхождения точки в окрестность другой точки
     *
     * @param x1 - точка, в окресность которой проверяем вхождение
     * @param x2 - исследуемая точка
     * @return <b>true</b> - если x2 входит в окресность x1 <br>
     * <b>false</b> - если x2 не входит в окресность x1
     */
    private boolean insidePoint(double x1, double x2) {
        final double e = 1e-8; // некоторая малая величина
        return (x2 <= x1 + e) && (x2 >= x1 - e);
    }

    private BIMExt loadBim(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        BIMLoader<BIMExt> bimLoader = new BIMLoader<>(fis, BIMExt.class);

        return bimLoader.getBim();
    }

    public void saveFileHandler() {
    }

    public void modeViewHandler() {
    }

    public void addSensorHandler() {
    }

    public void addLightHandler() {
        myScale.set(myScale.getValue() - 0.5);
    }

    public void addArrowHandler() {
        myScale.set(myScale.getValue() + 0.5);
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        primaryStage = Main.getPrimaryStage();

        canvas.toBack();

        canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            gRoot.setLayoutX(0);
            gRoot.prefWidth(newValue.doubleValue());
        });
        canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            gRoot.setLayoutY(newValue.doubleValue());
            gRoot.prefHeight(newValue.doubleValue());
        });

        // Create operator
        AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

        // Listen to scroll events (similarly you could listen to a button click, slider, ...)
        gRoot.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0) {
                    // zoom out
                    zoomFactor = 1 / zoomFactor;
                }
                zoomOperator.zoom(gRoot, zoomFactor, event.getSceneX(), event.getSceneY());
            }
        });

    }

    class AnimatedZoomOperator {

        private Timeline timeline;

        public AnimatedZoomOperator() {
            this.timeline = new Timeline(60);
        }

        public void zoom(Node node, double factor, double x, double y) {
            // determine scale
            double oldScale = node.getScaleX();
            double scale = oldScale * factor;
            double f = (scale / oldScale) - 1;

            // determine offset that we will have to move the node
            Bounds bounds = node.localToScene(node.getBoundsInLocal());
            double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
            double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

            double time = 100;
            // timeline that scales and moves the node
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(time),
                            new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                    new KeyFrame(Duration.millis(time),
                            new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                    new KeyFrame(Duration.millis(time), new KeyValue(node.scaleXProperty(), scale)),
                    new KeyFrame(Duration.millis(time), new KeyValue(node.scaleYProperty(), scale)));
            timeline.play();
        }
    }
}
