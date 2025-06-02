package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;

import com.example.demo.util.ScreenUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        ArrayList<Double> dimensions = new ScreenUtil().getScreenDimensions(0.8, 0.9);
        double windowWidth = dimensions.get(0);
        double windowHeight = dimensions.get(1);

        FXMLLoader fxmlLoader = new FXMLLoader(
                Application.class.getResource("views/loginPage-view.fxml")
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
