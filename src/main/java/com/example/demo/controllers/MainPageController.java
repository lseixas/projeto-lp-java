package com.example.demo.controllers;

import com.example.demo.models.entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    User loggedUser;

    public void initData(User userFromLastPage) {
        loggedUser = userFromLastPage;
        System.out.println("User logged in: " + loggedUser.getNome());
    }

    public void redirectScreen(String viewFileName, Node randomNode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/" + viewFileName));
        Parent root = loader.load();

        // get current stage from any node
        Stage stage = (Stage) randomNode.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleAccountButtonClick(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/accountDisplay-view.fxml"));
                Parent root = loader.load();

                AccountDisplayController accountDisplayController = loader.getController();
                accountDisplayController.initData(loggedUser);

                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void handleDepositButtonClick(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            try {
                redirectScreen("depositPage-view.fxml", (Node) mouseEvent.getSource());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleWithdrawButtonClick(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            try {
                redirectScreen("withdrawPage-view.fxml", (Node) mouseEvent.getSource());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
