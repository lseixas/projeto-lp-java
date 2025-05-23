package com.example.demo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("views/loginPage-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 500, 	500);
javafx.scene.image.Image logo = new javafx.scene.image.Image(
    HelloApplication.class.getResource("/com/example/demo/views/reusable/logo.png").toExternalForm(),
    64, // largura desejada
    64, // altura desejada
    true, // preservar proporção
    true // suavização
);
stage.getIcons().add(logo);
        stage.setTitle("Banco CVETTI");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}