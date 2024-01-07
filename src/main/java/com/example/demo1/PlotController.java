package com.example.demo1;

import com.example.presentation.DotController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class PlotController {

    private final Plot plot = new Plot(
            "X",
            0,
            800,
            50,
            "Y",
            0,
            600,
            50
    );

    @FXML
    private AnchorPane root;

    private final List<Point2D> points = new ArrayList<>();

    public void init(DotController controller) {
        root.getChildren().add(plot);
        controller.addOnMainDotPositionListener((observableValue, coords, t1) -> {
            synchronized (points) {
                points.add(new Point2D(t1.x(), t1.y()));
            }
        });
    }

    public void renderPlot() {
        Platform.runLater(
                () -> {
                    synchronized (points) {
                        for (Point2D point : points) {
                            plot.addPoint(point);
                        }
                        plot.renderAllPlot();
                    }
                }
        );
    }
}
