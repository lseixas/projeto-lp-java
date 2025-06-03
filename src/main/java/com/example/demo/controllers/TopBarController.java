package com.example.demo.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TopBarController {

    public void handleBackToHome(MouseEvent mouseEvent) throws IOException {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
            Parent root = loader.load();

            // get current stage from any node
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }


    public void handleLogoClick(MouseEvent mouseEvent) throws IOException {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
            Parent root = loader.load();

            // get current stage from any node
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void handleExitButton(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY){
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
