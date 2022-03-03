package com.cmpt.pngviewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageController {
    @FXML
    Canvas canvas;
    @FXML
    AreaChart<Number, Number> redChart;
    @FXML
    AreaChart<Number, Number> greenChart;
    @FXML
    AreaChart<Number, Number> blueChart;

    public static void show() {
        App.setRoot("image-view");
    }

    public void initialize() {
        if (App.file != null && App.file.isFile() && App.file.exists()) {
            Image image;
            try {
                image = new Image(new FileInputStream(App.file.getPath()));
                PixelReader reader = image.getPixelReader();

                GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
                int height = (int) image.getHeight();
                int width = (int) image.getWidth();

                System.out.printf("%d %d\n", height, width);

                int[] red = new int[255];
                int[] green = new int[255];
                int[] blue = new int[255];

                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        Color color = reader.getColor(i, j);

                        red[(int) Math.floor(color.getRed() * 255)]++;
                        green[(int) Math.floor(color.getGreen() * 255)]++;
                        blue[(int) Math.floor(color.getBlue() * 255)]++;

                        System.out.printf("R:%f G:%f B:%f\n", color.getRed(), color.getGreen(), color.getBlue());
                        graphicsContext2D.setFill(color);
                        graphicsContext2D.fillRect(i, j, 1, 1);
                    }
                }

                ObservableList<XYChart.Series<Number, Number>> redSeries = FXCollections.observableArrayList();
                ObservableList<XYChart.Series<Number, Number>> blueSeries = FXCollections.observableArrayList();
                ObservableList<XYChart.Series<Number, Number>> greenSeries = FXCollections.observableArrayList();

                ObservableList<XYChart.Data<Number, Number>> redData = FXCollections.observableArrayList();
                ObservableList<XYChart.Data<Number, Number>> blueData = FXCollections.observableArrayList();
                ObservableList<XYChart.Data<Number, Number>> greenData = FXCollections.observableArrayList();

                for (int i = 0; i < 255; i++) {
                    redData.add(new XYChart.Data<>(i, red[i]));
                    blueData.add(new XYChart.Data<>(i, blue[i]));
                    greenData.add(new XYChart.Data<>(i, green[i]));
                }

                redSeries.add(new XYChart.Series<>(redData));
                blueSeries.add(new XYChart.Series<>(blueData));
                greenSeries.add(new XYChart.Series<>(greenData));

                redChart.setData(redSeries);
                blueChart.setData(blueSeries);
                greenChart.setData(greenSeries);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}