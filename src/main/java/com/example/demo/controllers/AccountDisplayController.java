package com.example.demo.controllers;

import com.example.demo.models.entities.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class AccountDisplayController {


    public Label nomeLabel;
    public Label cpfLabel;
    public Label emailLabel;
    public Label nascimentoLabel;
    public Label saldoLabel;

    User loggedUser;

    public void initData(User user) {
        System.out.println("Initializing AccountDisplayController with user: " + user.getNome());
        loggedUser = user;

        nomeLabel.setText(loggedUser.getNome());
        cpfLabel.setText(loggedUser.getCpf());
        emailLabel.setText(loggedUser.getEmail());
        nascimentoLabel.setText(loggedUser.getNascimento().toString());
        saldoLabel.setText(String.valueOf(loggedUser.getSaldo()));
    }

    public void initialize() {

    }

    public void handleEditAccount(ActionEvent actionEvent) {
    }

    public void handleChangePassword(ActionEvent actionEvent) {
    }

    public void handleBackToHome(ActionEvent actionEvent) {
    }
}
