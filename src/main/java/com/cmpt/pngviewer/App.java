package com.cmpt.pngviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

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
        uploadFile(stage);
        scene = new Scene(Objects.requireNonNull(loadFXML("image-view")), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public static void uploadFile(Window stage) {
        FileChooser fileChooser = new FileChooser();
        try {
            App.file = fileChooser.showOpenDialog(stage);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

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

    public static void exit(){
        System.exit(0);
    }
}