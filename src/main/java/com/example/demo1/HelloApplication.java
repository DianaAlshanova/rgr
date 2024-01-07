package com.example.demo1;

import com.example.presentation.Dot;
import com.example.presentation.DotController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movement of dot");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        var controller = new DotController(5, 500, 1488);
        controller.addOnMainDotPositionListener((observableValue, coords, t1) -> {
            Platform.runLater(
                () -> {
                    Dot.Coords newCords = t1;
                }
            );
        });
    }
}