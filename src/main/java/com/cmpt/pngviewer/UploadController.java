package com.cmpt.pngviewer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class UploadController {

    @FXML
    public Button uploadButton;

    public static void show() {
        App.setRoot("upload-view");
    }

    @FXML
    protected void onUploadClick(Event event) {
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getTarget();
        try {
            App.file = fileChooser.showOpenDialog(node.getScene().getWindow());
            ImageController.show();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}