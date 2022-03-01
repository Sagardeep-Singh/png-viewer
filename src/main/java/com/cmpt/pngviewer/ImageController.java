package com.cmpt.pngviewer;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageController {
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane anchorPane;

    public static void show() {
        App.setRoot("image-view");
    }

    public void initialize() {
        if (App.file != null && App.file.isFile() && App.file.exists()) {

            Image image;
            try {
                image = new Image(new FileInputStream(App.file.getPath()));
                canvas.getGraphicsContext2D().drawImage(image, 0, 0, image.getWidth(), image.getHeight());
                anchorPane.setMinWidth(image.getWidth());
                anchorPane.setMinHeight(image.getHeight());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}