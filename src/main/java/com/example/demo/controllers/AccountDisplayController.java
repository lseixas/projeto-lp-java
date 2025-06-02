package com.example.demo.controllers;

import com.example.demo.models.entities.User;
import com.example.demo.util.Global;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class AccountDisplayController {


    public Label nomeLabel;
    public Label cpfLabel;
    public Label emailLabel;
    public Label nascimentoLabel;
    public Label saldoLabel;

    static User loggedUser;

    public void initialize() throws SQLException {

        loggedUser = Global.getLoggedInUser();
        nomeLabel.setText(loggedUser.getNome());
        cpfLabel.setText(loggedUser.getCpf());
        emailLabel.setText(loggedUser.getEmail());
        nascimentoLabel.setText(loggedUser.getNascimento().toString());
        saldoLabel.setText(String.valueOf(loggedUser.getSaldo()));

    }

    public void handleEditAccount(ActionEvent actionEvent) {
    }

    public void handleChangePassword(ActionEvent actionEvent) {
    }

}
