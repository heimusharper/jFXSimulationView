package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        AnchorPane root = loader.load();  
        primaryStage.setTitle("FXSimulationView");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }
}
