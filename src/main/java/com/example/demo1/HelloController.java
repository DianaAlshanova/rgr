package com.example.demo1;

import com.example.presentation.DotController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable
{
    private final DotController controller;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button buttonStat;
    @FXML
    private Button plotButton;
    @FXML
    private Button velocityPlotButton;

    public HelloController() {
        this.controller = new DotController(3, 800, 600);
    }

    @FXML
    public void createStatisticController() throws IOException {
        FXMLLoader fxmlStat = new FXMLLoader(StatisticController.class.getResource("statistic-view.fxml"));
        Parent root = (Parent) fxmlStat.load();
        StatisticController ctrl = fxmlStat.getController();
        ctrl.init(controller);
        Scene sceneStat = new Scene(root);
        Stage stageStat = new Stage();
        stageStat.setScene(sceneStat);
        stageStat.show();
    }

    public void createPlotController() throws IOException {
        FXMLLoader loader = new FXMLLoader(PlotController.class.getResource("plot-view.fxml"));
        Parent root = loader.load();
        PlotController ctrl = loader.getController();
        ctrl.init(controller);
        Scene scenePlot = new Scene(root);
        Stage statePlot = new Stage();
        statePlot.setScene(scenePlot);
        statePlot.show();
    }

    public void createVelocityPlotController() throws IOException {
        FXMLLoader loader = new FXMLLoader(PlotController.class.getResource("velocity-plot-view.fxml"));
        Parent root = loader.load();
        VelocityPlotController ctrl = loader.getController();
        ctrl.init(controller);
        Scene scenePlot = new Scene(root);
        Stage statePlot = new Stage();
        statePlot.setScene(scenePlot);
        statePlot.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Circle circle = new Circle();
        circle.setRadius(10);
        mainPane.getChildren().add(circle);
        for(int i = 0; i <= controller.getDots().length - 2; i++) {
            Circle dotsCircle = new Circle();
            dotsCircle.setRadius(10);
            dotsCircle.setCenterX(controller.getDots()[i].getP().x());
            dotsCircle.setCenterY(controller.getDots()[i].getP().y());
            mainPane.getChildren().add(dotsCircle);
        }
        controller.addOnMainDotPositionListener((observableValue, coords, t1) -> {
            Platform.runLater(
                    () -> {
                        circle.setCenterX(t1.x());
                        circle.setCenterY(t1.y());
                        mainPane.getChildren().add(new Circle(t1.x(), t1.y(), 2, Color.GREY));
                    }
            );
        });

        buttonStat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    createStatisticController();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        plotButton.setOnAction(actionEvent -> {
            try {
                createPlotController();
            } catch (Exception ignored) { }
        });

        velocityPlotButton.setOnAction(actionEvent -> {
            try {
                createVelocityPlotController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}