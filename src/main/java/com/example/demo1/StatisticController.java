package com.example.demo1;

import com.example.presentation.DotController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatisticController {

    public static class tableModel {
        private Double xCoordinate;
        private  Double yCoordinate;

        public tableModel(double x, double y) {
            this.xCoordinate = x;
            this.yCoordinate = y;
        }

        public double getXCoordinate() {
            return xCoordinate;
        }
        public double getYCoordinate() {
            return yCoordinate;
        }
        public void setXCoordinate(double x) {this.xCoordinate = x;}
        public void setYCoordinate(double y) {this.yCoordinate = y;}
    }

    @FXML
    private TableColumn<tableModel, Double> yColumn;

    @FXML
    private TableColumn<tableModel, Double> xColumn;

    @FXML
    private TableView<tableModel> tableView;

    private DotController controller;

    public void init(DotController controller) {
        this.controller = controller;
        System.out.println(tableView.getColumns());

        controller.addOnMainDotPositionListener((observableValue, coords, t1) -> {
            Platform.runLater(
                    () -> {
                        yColumn.setCellValueFactory(new PropertyValueFactory<>("yCoordinate"));
                        xColumn.setCellValueFactory(new PropertyValueFactory<>("xCoordinate"));
                        tableModel currentCoordinates = new tableModel(t1.x(), t1.y());
                        ObservableList<tableModel> dots_list = tableView.getItems();
                        dots_list.add(currentCoordinates);
                        tableView.setItems(dots_list);
                    }
             );
        });
    }
}
