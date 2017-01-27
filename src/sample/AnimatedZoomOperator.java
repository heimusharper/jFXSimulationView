package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animated Zoom
 * <p>
 * Created by boris on 27.01.17.
 */
class AnimatedZoomOperator {
    private Timeline timeline;

    AnimatedZoomOperator() {
        this.timeline = new Timeline(60);
    }

    void zoom(Node node, double factor, double x, double y) {
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
