package com.cmpt.pngviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    static File file;

    @Override
    public void start(Stage stage) {
        scene = new Scene(Objects.requireNonNull(loadFXML("upload-view")), 640, 480);
        stage.setScene(scene);
        stage.show();    }

    static void setRoot(String fxml) {
        Parent parent = loadFXML(fxml);
        scene.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch();
    }

}