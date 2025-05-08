package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader2 = new FXMLLoader(
                HelloApplication.class.getResource("calendar-view.fxml")
        );
        Scene scene2 = new Scene(fxmlLoader2.load(), 500, 500);

        stage.setTitle("Hello!");
        stage.setScene(scene2);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}