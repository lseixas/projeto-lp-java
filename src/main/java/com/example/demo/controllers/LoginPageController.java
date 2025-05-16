package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginPageController {

    @FXML
    private TextField cpfTextField;
    @FXML
    private VBox centerLoginBox;
    @FXML
    private Button cpfLoginButton;

    private boolean extraLoaded = false;

    public void handleEnter(){

        String cpf = cpfTextField.getText();
        System.out.println("CPF inserido: " + cpf);

    }

    public void handleLoginButton(){

        String cpf = cpfTextField.getText();

        if (validateCpf(cpf)){

            System.out.println("CPF válido: " + cpf);
            addExtraInputPane();

        } else {
            System.out.println("CPF inválido: " + cpf);
        }

    }

    public boolean validateCpf(String cpf){
        String cpfRegex = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$";
        return Pattern.matches(cpfRegex, cpf);
    }

    private void addExtraInputPane() {
        if (extraLoaded) return;  // evita múltiplas inserções
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo/views/reusable/loginPage/loginPasswordBox-reusable.fxml")
            );
            Node passwordVBox = loader.load();

            int buttonIndex = centerLoginBox.getChildren().indexOf(cpfLoginButton);
            centerLoginBox.getChildren().add(buttonIndex, passwordVBox);

            extraLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Initialize(){

    }

}
