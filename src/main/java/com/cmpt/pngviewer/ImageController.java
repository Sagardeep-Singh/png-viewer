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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageController {
    public static final double OFFSET_X = 20;
    public static final double OFFSET_Y = 275;
    public static final double MARGIN_TOP = 25;
    public static final double HISTOGRAM_HEIGHT = OFFSET_Y - MARGIN_TOP;
    public static final int COLOR_SIZE = 255;
    public static final double BAR_WIDTH = 1.75;
    @FXML
    Canvas canvas;
    @FXML
    AnchorPane redHistogram, greenHistogram, blueHistogram;

    public static void show() {
        App.setRoot("image-view");
    }

    public void onUploadClick(Event event) {
        Node node = (Node) event.getTarget();
        App.uploadFile(node.getScene().getWindow());
        initialize();
    }

    public void onDitherClick(Event event) {
        DitherController.show();
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

                    canvas.resize(width, height);
                    GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
                    graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    double imageOffsetX = (canvas.getWidth() - width) / 2;
                    double imageOffsetY = (canvas.getHeight() - height) / 2;

                    int[] red = new int[COLOR_SIZE];
                    int[] green = new int[COLOR_SIZE];
                    int[] blue = new int[COLOR_SIZE];

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            Color color = reader.getColor(i, j);

                            red[(int) Math.floor(color.getRed() * (COLOR_SIZE - 1))]++;
                            green[(int) Math.floor(color.getGreen() * (COLOR_SIZE - 1))]++;
                            blue[(int) Math.floor(color.getBlue() * (COLOR_SIZE - 1))]++;

                            graphicsContext2D.setFill(color);
                            graphicsContext2D.fillRect(imageOffsetX + i, imageOffsetY + j, 1, 1);
                        }
                    }

                    int maxRed = 0, maxGreen = 0, maxBlue = 0;
                    for (int i = 0; i < COLOR_SIZE; i++) {
                        maxRed = Math.max(maxRed, red[i]);
                        maxGreen = Math.max(maxGreen, green[i]);
                        maxBlue = Math.max(maxBlue, blue[i]);
                    }

                    redHistogram.getChildren().clear();
                    greenHistogram.getChildren().clear();
                    blueHistogram.getChildren().clear();

                    for (int i = 0; i < COLOR_SIZE; i++) {
                        double x = OFFSET_X + (i * BAR_WIDTH);
                        Line redLine = new Line(x,
                                OFFSET_Y,
                                x,
                                MARGIN_TOP + (HISTOGRAM_HEIGHT * (1 - (red[i] / (double) maxRed))));
                        redLine.setStrokeWidth(BAR_WIDTH);
                        redLine.setStroke(Color.RED);

                        Line greenLine = new Line(x,
                                OFFSET_Y,
                                x,
                                MARGIN_TOP + (HISTOGRAM_HEIGHT * (1 - (green[i] / (double) maxGreen))));
                        greenLine.setStrokeWidth(BAR_WIDTH);
                        greenLine.setStroke(Color.GREEN);

                        Line blueLine = new Line(x,
                                OFFSET_Y,
                                x,
                                MARGIN_TOP + (HISTOGRAM_HEIGHT * (1 - (blue[i] / (double) maxBlue))));
                        blueLine.setStrokeWidth(BAR_WIDTH);
                        blueLine.setStroke(Color.BLUE);

                        Platform.runLater(()->{
                            redHistogram.getChildren().add(redLine);
                            greenHistogram.getChildren().add(greenLine);
                            blueHistogram.getChildren().add(blueLine);
                        });
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onExitClick(ActionEvent actionEvent) {
        App.exit();
    }
}