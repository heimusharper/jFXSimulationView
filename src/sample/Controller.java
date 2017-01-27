package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.extendetGeometry.BIMExt;
import json.extendetGeometry.BIMLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML public Pane       canvas;
    @FXML public AnchorPane content;
    @FXML public Accordion  rightPanel;
    @FXML public ToolBar    toolBar;
    @FXML public Group      gRoot;

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
            FXBIMExtHandler fxbimExtHandler = new FXBIMExtHandler(bim);
            fxbimExtHandler.drawBIM(gRoot);
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

    public void modeViewHandler() {
    }

    public void addSensorHandler() {
    }

    public void addLightHandler() {
    }

    public void addArrowHandler() {
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
    }
}
