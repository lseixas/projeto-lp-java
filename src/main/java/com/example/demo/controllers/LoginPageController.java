package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.regex.Pattern;

public class LoginPageController {

    @FXML
    private TextField cpfTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private VBox centerLoginBox;
    @FXML
    private VBox passwordContainer;
    @FXML
    private Button cpfLoginButton;

    private boolean extraLoaded = false;

    //handler for enter pressed

    public void handleEnterPressed(){

        String cpf = cpfTextField.getText();
        System.out.println("CPF inserido: " + cpf);
        handleLoginButtonClick();

    }

    //handler for button click

    public void handleLoginButtonClick(){

        String cpf = cpfTextField.getText();
        if(extraLoaded){
            String password = passwordTextField.getText();
            logicCpfPasswordInsert();
        }
        else {
            logicCpfInsert();
        }
    }

    //logic for cpf + password insertion

    public void logicCpfPasswordInsert(){
        String cpf = cpfTextField.getText();
        String password = passwordTextField.getText();

        if (validateCpf(cpf) && validadePassword(password)){
            System.out.println("CPF e senha válidos: " + cpf + ", " + password);
        } else {
            System.out.println("CPF ou senha inválidos: " + cpf + ", " + password);
        }
    }

    //logic for first insertion

    public void logicCpfInsert(){

        String cpf = cpfTextField.getText();

        if (validateCpf(cpf)){

            System.out.println("CPF válido: " + cpf);
            addExtraInputPane();
            extraLoaded = true;

            String password = passwordTextField.getText();
            System.out.println("Senha inserida: " + password);

        } else {
            System.out.println("CPF inválido: " + cpf);
        }

    }

    //visible password text field

    private void addExtraInputPane() {
        passwordContainer.setVisible(true);
        passwordContainer.setManaged(true);
    }


    //field validations

    public boolean validateCpf(String cpf){
        String cpfRegex = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$";
        return Pattern.matches(cpfRegex, cpf);
    }

    public boolean validadePassword(String password){
        String passwordRegex = "^.{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    public void Initialize(){

    }

}
