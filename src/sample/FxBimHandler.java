package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import json.extendetGeometry.BIMExt;
import json.extendetGeometry.SensorExt;
import json.extendetGeometry.TransitionExt;
import json.extendetGeometry.ZoneExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static json.geometry.Zone.FLOOR;

/**
 * fx BIM Handler
 * <p>
 * Created by boris on 27.01.17.
 */
class FxBimHandler {

    private static final Logger log = LoggerFactory.getLogger(FxBimHandler.class);

    private BIMExt     bim;
    private Controller controller;
    private double     orgSceneX;
    private double     orgSceneY;
    private double     orgTranslateX;
    private double     orgTranslateY;

    FxBimHandler(BIMExt bim, Controller controller) {
        this.bim = bim;
        this.controller = controller;
    }

    void drawBim(Group gRoot) {
        gRoot.getChildren().remove(0, gRoot.getChildren().size());
        setZoomer(gRoot);
        setDraggable(gRoot);

        ArrayList<ZoneExt> zones = new ArrayList<>(bim.getRooms().size());
        bim.getRooms().parallelStream().forEach(z -> z.getZones().parallelStream().forEach(zones::add));

        List<Double> levels = zones.parallelStream().filter(e -> e.getType() == FLOOR /*|| e.getType() == STAIRS*/)
                .map(e -> e.getXyz(0, 0, 2)).distinct().sorted().collect(Collectors.toList());

        for (int i = 0; i < levels.size(); i++) {
            drawLevel(gRoot, levels, i);
        }

        searchNodeHandler(gRoot);
    }

    private void searchNodeHandler(Group gRoot) {
        controller.searchNode.setOnMouseClicked((MouseEvent event) -> {
            String uuidNode = controller.searchNodeField.getText();
            if (uuidNode.isEmpty()) return;

            ObservableList<Node> allGroups = gRoot.getChildren();
            for (Node allGroup : allGroups) {
                ObservableList<Node> node = ((Group) allGroup).getChildren();
                for (Node aNode : node) {
                    Shape shape = (Shape) aNode;
                    String uuid = shape.getId();
                    if (uuid == null) continue; // Не знаю, почему вылазит null
                    if (!uuid.equals(uuidNode)) continue;
                    shape.setFill(Color.TOMATO);
                    return;
                }
            }
        });
    }

    private void drawLevel(Group gRoot, List<Double> levels, int i) {
        double currentLevel = levels.get(i);
        int numOfLevels = levels.size();

        Group gZones = new Group();
        gZones.setId("gZones_" + i);
        Group gTransitions = new Group();
        gTransitions.setId("gTransitions_" + i);
        Group gSensors = new Group();
        gSensors.setId("gSensors_" + i);
        Group gArrows = new Group();
        gArrows.setId("gArrows_" + i);
        Group gLights = new Group();
        gLights.setId("gLights_" + i);
        gRoot.getChildren().addAll(gZones, gTransitions, gSensors, gArrows, gLights);
        gRoot.setId("gRoot");

        for (ZoneExt zone : bim.getZones().values()) {
            if (zone.isSafetyZone()) {
                //                iter.remove();
                continue; // Отсекаем безопасную зону, она не имеет геометрических параметров
            }

            // Комнаты и лестницы отображаем по-разному
            switch (zone.getType()) {
            case FLOOR:
                if (!insidePoint(currentLevel, zone.getLevel())) continue;
                gZones.getChildren().add(drawZone(bim, numOfLevels, zone));
                break;
            default:
                gZones.getChildren().add(drawZone(bim, numOfLevels, zone));
            }

            if (!zone.getSensors().isEmpty()) for (SensorExt sensor : zone.getSensors())
                gSensors.getChildren().add(drawSensor(sensor, numOfLevels));

            if (!zone.getTransitionList().isEmpty()) for (TransitionExt transition : zone.getTransitionList())
                gTransitions.getChildren().add(drawTransition(transition, numOfLevels));
        }
    }

    private Polygon drawTransition(TransitionExt transition, int numOfLevels) {
        int pointsNumber = transition.getXyz().length;
        Polygon p = new Polygon();
        if (pointsNumber > 2) {
            // Создание полигонов
            for (int j = 0; j < pointsNumber; j++) {
                double x = transition.getXyz(j, 0) * 10.0;
                double y = (numOfLevels - transition.getXyz(j, 1)) * 10.0;
                p.getPoints().addAll(x, y);
            }
            p.setFill(transition.isExit() ? Color.CADETBLUE : Color.HOTPINK);
            p.setOnMouseClicked(event -> {
                controller.transitionTab.setExpanded(true);
                fillTransitionData(transition);
            });
            p.setId(transition.getId());
        } else if (pointsNumber == 2) {
            /*Line p = new Line();
            p.setStartX(transition.getXyz(0, 0) * 10.0);
            p.setStartY((numOfLevels - transition.getXyz(0, 1)) * 10.0);
            p.setEndX(transition.getXyz(1, 0) * 10.0);
            p.setEndY((numOfLevels - transition.getXyz(1, 1)) * 10.0);
            p.setStroke(transition.isExit() ? Color.GOLD : Color.HOTPINK);
            p.setId(transition.getId());
            gTransitions.getChildren().add(p);*/
        }
        return p;
    }

    private Circle drawSensor(SensorExt sensor, double s) {
        final double x = sensor.getX() * 10.0;
        final double y = (s - sensor.getY()) * 10.0;
        Circle c = new Circle(x, y, 5, Color.BLUEVIOLET);
        c.setOnMousePressed(event -> {
            boolean isEditable = false;
            if (event.isControlDown()) isEditable = true;
            fillSensorData(sensor, isEditable);
        });

        c.setId(c.getId());

        return c;
    }

    private Polygon drawZone(BIMExt bim, double s, ZoneExt z) {
        final Polygon p = new Polygon();

        for (int c = 0; c < z.getXyz().length; c++) // кольца
            for (int j = 0; j < z.getXyz()[0].length; j++) {  // точки
                double x = z.getXyz(c, j, 0) * 10.0;
                double y = (s - z.getXyz(c, j, 1)) * 10.0;
                p.getPoints().addAll(x, y);
            }

        p.setId(z.getId());
        p.setOnMousePressed(event -> {
            controller.zoneTab.setExpanded(true);
            fillZoneData(z);
        });

        int valFill = (int) ((z.getNumOfPeople() * 255) / bim.getNumOfPeople()) * 10;
        p.setFill(Color.rgb(0, valFill > 255 ? 255 : valFill, 0));

        z.numberOfPeopleProperty().addListener((e, oldValue, newValue) -> {
            final double val = ((newValue.doubleValue() * 255) / bim.getNumOfPeople()) * 10;
            final int colorVal = (int) val > 255 ? 255 : (int) val;
            p.setFill(Color.rgb(0, colorVal, 0));
        });

        z.permeabilityProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 0.1) p.setFill(Color.RED);
        });

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

    private void setZoomer(Group gRoot) {
        // Create operator
        AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

        // Listen to scroll events (similarly you could listen to a button click, slider, ...)
        gRoot.setOnScroll(event -> {
            double zoomFactor = 1.5;
            if (event.isControlDown()) zoomFactor = 1.1;
            if (event.getDeltaY() <= 0) {
                // zoom out
                zoomFactor = 1 / zoomFactor;
            }
            zoomOperator.zoom(gRoot, zoomFactor, event.getSceneX(), event.getSceneY());
        });
    }

    /**
     * http://java-buddy.blogspot.ru/2013/07/javafx-drag-and-move-something.html
     */
    private void setDraggable(Group gRoot) {
        gRoot.setOnMousePressed(mousePressedEvent -> {
            orgSceneX = mousePressedEvent.getSceneX();
            orgSceneY = mousePressedEvent.getSceneY();
            orgTranslateX = ((Group) (mousePressedEvent.getSource())).getTranslateX();
            orgTranslateY = ((Group) (mousePressedEvent.getSource())).getTranslateY();
        });

        gRoot.setOnMouseDragged(mouseDraggedEvent -> {
            double offsetX = mouseDraggedEvent.getSceneX() - orgSceneX;
            double offsetY = mouseDraggedEvent.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((Group) (mouseDraggedEvent.getSource())).setTranslateX(newTranslateX);
            ((Group) (mouseDraggedEvent.getSource())).setTranslateY(newTranslateY);
        });
    }

    private void fillZoneData(ZoneExt z) {
        controller.zoneIdField.setText(z.getId());
        controller.zoneNumOfPeopleField.setText(String.valueOf(z.getNumOfPeople()));
    }

    private void fillSensorData(SensorExt s, boolean isEditable) {
        controller.sensorTab.setExpanded(true); // Разворачиваем вкладку при активации сенсора
        controller.sensorIdField.setText(s.getId());
        controller.sensorDeviceIdField.setText(String.valueOf(s.getDeviceId()));

        String[] types = new String[] { "UNKNOWN", "TEMPERATURE", "SMOKE", "GENERAL" };
        controller.sensorTypeField.setItems(FXCollections.observableArrayList(types));

        int index;
        switch (s.getType()) {
        case "TEMPERATURE":
            index = 1;
            break;
        case "SMOKE":
            index = 2;
            break;
        case "GENERAL":
            index = 3;
            break;
        default:
            index = 0;
        }
        controller.sensorTypeField.getSelectionModel().select(index);

        /*if (isEditable) {
            controller.sensorIdField.setEditable(true);
            controller.sensorDeviceIdField.setEditable(true);
            controller.sensorTypeField.setDisable(false);
        }*/
    }

    private void applyChangeSensor() {

    }

    private void fillTransitionData(TransitionExt t) {
        controller.transitionIdField.setText(t.getId());
        controller.transitionWidthField.setText(String.valueOf(t.getWidth()));
    }

}
