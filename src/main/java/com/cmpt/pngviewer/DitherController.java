package com.cmpt.pngviewer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DitherController {

    public static double[][] DITHER_MATRIX = {
            {1 / 16.0, 9 / 16.0, 3 / 16.0, 11 / 16.0},
            {13 / 16.0, 5 / 16.0, 15 / 16.0, 7 / 16.0},
            {4 / 16.0, 12 / 16.0, 2 / 16.0, 10 / 16.0},
            {16 / 16.0, 8 / 16.0, 14 / 16.0, 6 / 16.0}
    };

    //    public static double[][] DITHER_MATRIX = {
//            {-0.25, 0.25},
//            {0.5, 0},
//    };
    @FXML
    public Canvas originalCanvas, ditheredCanvas;

    public static void show() {
        App.setRoot("dither-view");
    }

    public void onUploadClick(Event event) {
        Node node = (Node) event.getTarget();
        App.uploadFile(node.getScene().getWindow());
        initialize();
    }

    public void onHistogramClick(Event event) {
        ImageController.show();
    }

    public void initialize() {
        Platform.runLater(() -> {
            if (App.file != null && App.file.isFile() && App.file.exists()) {
                Image image;
                try {
                    image = new Image(new FileInputStream(App.file.getPath()));
                    PixelReader reader = image.getPixelReader();

                    int height = (int) image.getHeight();
                    int width = (int) image.getWidth();

                    Color[][] imageArray = new Color[width][height];

                    double imageOffsetX = (originalCanvas.getWidth() - width) / 2;
                    double imageOffsetY = (originalCanvas.getHeight() - height) / 2;

                    GraphicsContext originalGC = originalCanvas.getGraphicsContext2D();
                    originalGC.clearRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            Color color = reader.getColor(i, j);

                            imageArray[i][j] = color;
                            originalGC.setFill(color);
                            originalGC.fillRect(imageOffsetX + i, imageOffsetY + j, 1, 1);
                        }
                    }

                    GraphicsContext ditheredGC = ditheredCanvas.getGraphicsContext2D();
                    ditheredGC.clearRect(0, 0, ditheredCanvas.getWidth(), ditheredCanvas.getHeight());

                    imageOffsetX = (ditheredCanvas.getWidth() - width) / 2;
                    imageOffsetY = (ditheredCanvas.getHeight() - height) / 2;

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            Color color = imageArray[i][j];

                            int m = i % DITHER_MATRIX.length;
                            int n = j % DITHER_MATRIX[m].length;

                            double value = DITHER_MATRIX[m][n] - 0.5;
                            double red = getPaletteColor(color.getRed(), value);
                            double green = getPaletteColor(color.getGreen(), value);
                            double blue = getPaletteColor(color.getBlue(), value);
                            ditheredGC.setFill(new Color(red, green, blue, color.getOpacity()));
                            ditheredGC.fillRect(imageOffsetX + i, imageOffsetY + j, 1, 1);
                        }
                    }

                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    private double getPaletteColor(double v, double t) {
        return v > t ? 1 : 0;
    }

    public void onExitClick(ActionEvent actionEvent) {
        App.exit();
    }
}
