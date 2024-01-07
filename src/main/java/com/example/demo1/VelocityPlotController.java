package com.example.demo1;

import com.example.presentation.Dot;
import com.example.presentation.DotController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.util.Map;
import java.util.TreeMap;

public class VelocityPlotController {

    private final Plot plot = new Plot(
            "Time",
            0,
            10,
            1000,
            "Velocity",
            0,
            0.1,
            0.01
    );

    @FXML
    private AnchorPane root;

    private final Map<Long, Double> velocities = new TreeMap<>();
    private Long startTime;

    public void init(DotController controller) {
        root.getChildren().add(plot);
        startTime = System.currentTimeMillis();
        controller.addOnMainDotVelocityListener((observableValue, coords, t1) -> {
            synchronized (velocities) {
                velocities.put(System.currentTimeMillis(), calculateVelocity(t1));
            }
        });
    }

    public void renderPlot() {
        Platform.runLater(
                () -> {
                    synchronized (velocities) {
                        for (Long time : velocities.keySet()) {
                            plot.addPoint(new Point2D((time - startTime), velocities.get(time)));
                        }
                        plot.renderAllPlot();
                    }
                }
        );
    }

    private Double calculateVelocity(Dot.Coords coords) {
        return Math.sqrt(
                Math.pow(coords.x(), 2) + Math.pow(coords.y(), 2)
        );
    }
}
