package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;

import com.example.demo.util.screenUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        ArrayList<Double> dimensions = new screenUtil().getScreenDimensions(0.8, 0.9);
        double windowWidth = dimensions.get(0);
        double windowHeight = dimensions.get(1);

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("views/loginPage-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);

        // Configura o título e as dimensões da janela
        stage.setTitle("Banco CVETTI");
        stage.setScene(scene);
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
