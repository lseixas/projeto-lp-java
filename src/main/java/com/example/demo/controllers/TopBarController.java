package com.example.demo.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TopBarController {

    public void handleLogoClick(MouseEvent event) {
        try {
            // Carrega o FXML da tela de login
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/views/loginPage-view.fxml"));
            // Obtém o Stage atual a partir do evento
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            // Troca a cena
            stage.setScene(new Scene(root, 500, 500));
        } catch (IOException e) {
        }
    }
}
