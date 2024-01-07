package com.example.demo1;

import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Plot extends ScrollPane {

    private static final double PREF_HEIGHT = 600;
    private static final double PREF_WIDTH = 800;

    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final XYChart.Series series = new XYChart.Series();
    private final Map<Long, Point2D> points = new TreeMap<>();

    public Plot(
            String xLabel,
            double minXValue,
            double maxXValue,
            double xStep,
            String yLabel,
            double minYValue,
            double maxYValue,
            double yStep
    ) {
        this.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        xAxis = new NumberAxis(xLabel, minXValue, maxXValue, xStep);
        yAxis = new NumberAxis(yLabel, minYValue, maxYValue, yStep);
        ScatterChart<String, Number> chart = new ScatterChart(xAxis, yAxis);
        chart.setPrefSize(PREF_WIDTH - 20.0, PREF_HEIGHT - 20.0);
        chart.getData().addAll(series);
        this.setContent(chart);
    }

    public void addPoint(Point2D point) {
        calculateNewBounds(point);
        points.put(System.currentTimeMillis(), point);
    }

    public void renderAllPlot() {
        series.getData().clear();
        for (Point2D value : points.values()) {
            series.getData().add(new XYChart.Data(value.getX(), value.getY()));
        }
    }

    public void renderLastPoints(int count) {
        series.getData().clear();
        var addTime = new ArrayList<>(points.keySet().stream().toList());
        Collections.reverse(addTime);
        try {
            for (int i = 0; i < count; i++) {
                series.getData().add(new XYChart.Data(points.get(addTime.get(i)).getX(), points.get(addTime.get(i)).getY()));
            }
        } catch (Exception ignored) {}
    }

    public Map<Long, Point2D> getLastPoints(int count) {
        final Map<Long, Point2D> localMap = new TreeMap<>();
        var addTime = new ArrayList<>(points.keySet().stream().toList());
        Collections.reverse(addTime);
        try {
            for (int i = 0; i < count; i++) {
                localMap.put(addTime.get(i), points.get(addTime.get(i)));
            }
        } catch (Exception ignored) {}

        return localMap;
    }

    private void calculateNewBounds(Point2D point) {
        if (point.getX() >= xAxis.getUpperBound()) {
            xAxis.setUpperBound(xAxis.getUpperBound() + (point.getX() - xAxis.getUpperBound()));
        }

        if (point.getY() >= yAxis.getUpperBound()) {
            yAxis.setUpperBound(yAxis.getUpperBound() + (point.getY() - yAxis.getUpperBound()));
        }

        if (point.getX() <= xAxis.getLowerBound()) {
            xAxis.setLowerBound(xAxis.getLowerBound() - (xAxis.getLowerBound() - point.getX()));
        }

        if (point.getY() <= yAxis.getLowerBound()) {
            yAxis.setLowerBound(yAxis.getLowerBound() - (yAxis.getLowerBound() - point.getY()));
        }
    }
}
