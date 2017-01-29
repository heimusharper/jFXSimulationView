package sample;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import json.extendetGeometry.BIMExt;
import json.extendetGeometry.SensorExt;
import json.extendetGeometry.TransitionExt;
import json.extendetGeometry.ZoneExt;

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
    }

    private void drawLevel(Group gRoot, List<Double> levels, int i) {
        double currentLevel = levels.get(i);
        int numOfLevels = levels.size();

        Group gZones = new Group();
        Group gTransitions = new Group();
        Group gSensors = new Group();
        Group gArrows = new Group();
        Group gLights = new Group();
        gRoot.getChildren().addAll(gZones, gTransitions, gSensors, gArrows, gLights);

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
                gZones.getChildren().add(drawZone(bim, numOfLevels, zone, iter));
                break;
            default:
                gZones.getChildren().add(drawZone(bim, numOfLevels, zone, iter));
            }

            if (!zone.getSensors().isEmpty()) for (SensorExt sensor : zone.getSensors()) {
                //if(isOnLevel(sensor.getZ(), level, zone.getCeilingHeight())) {
                Circle c = drawSensor(sensor, numOfLevels);
                gSensors.getChildren().add(c);
                //}
            }

            if (!zone.getTransitionList().isEmpty()) for (TransitionExt transition : zone.getTransitionList()) {
                drawTransition(transition, gTransitions, numOfLevels);
            }
        }
    }

    private void drawTransition(TransitionExt transition, Group gTransitions, int numOfLevels) {
        int pointsNumber = transition.getXyz().length;
        if (pointsNumber > 2) {
            // Создание полигонов
            Polygon p = new Polygon();
            for (int j = 0; j < pointsNumber; j++) {
                double x = transition.getXyz(j, 0) * 10.0;
                double y = (numOfLevels - transition.getXyz(j, 1)) * 10.0;
                p.getPoints().addAll(x, y);
            }
            p.setFill(transition.isExit() ? Color.CADETBLUE : Color.HOTPINK);
            p.setOnMouseClicked(event -> {
                controller.transitionTab.setExpanded(true);
            });
            gTransitions.getChildren().add(p);
        } else if (pointsNumber == 2) {
            Line p = new Line();
            p.setStartX(transition.getXyz(0, 0) * 10.0);
            p.setStartY((numOfLevels - transition.getXyz(0, 1)) * 10.0);
            p.setEndX(transition.getXyz(1, 0) * 10.0);
            p.setEndY((numOfLevels - transition.getXyz(1, 1)) * 10.0);
            p.setStroke(transition.isExit() ? Color.GOLD : Color.HOTPINK);
            gTransitions.getChildren().add(p);
        }
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

        return c;
    }

    private Polygon drawZone(BIMExt bim, double s, ZoneExt z, Iterator<ZoneExt> iter) {
        final Polygon p = new Polygon();

        for (int c = 0; c < z.getXyz().length; c++) {  // кольца
            for (int j = 0; j < z.getXyz()[0].length; j++) {  // точки
                double x = z.getXyz(c, j, 0) * 10.0;
                double y = (s - z.getXyz(c, j, 1)) * 10.0;
                p.getPoints().addAll(x, y);
                p.setId(z.getId());
            }
        }

        p.setOnMousePressed(event -> {
            controller.zoneTab.setExpanded(true);
            fillZoneData(z);
        });

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
        controller.sensorTab.setExpanded(true);
        controller.sensorIdField.setText(s.getId());
        controller.sensorDeviceIdField.setText(String.valueOf(s.getDeviceId()));

        List<String> types = new ArrayList<>();
        types.add("UNKNOWN");
        types.add("TEMPERATURE");
        types.add("SMOKE");
        types.add("GENERAL");
        controller.sensorTypeField.setItems(FXCollections.observableList(types));

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

        if (isEditable) {
            controller.sensorIdField.setEditable(true);
            controller.sensorDeviceIdField.setEditable(true);
            controller.sensorTypeField.setDisable(false);
        }
    }

    private void fillTransitionData(TransitionExt t) {
        controller.transitionIdField.setText(t.getId());
        controller.transitionWidthField.setText(String.valueOf(t.getWidth()));
    }
}