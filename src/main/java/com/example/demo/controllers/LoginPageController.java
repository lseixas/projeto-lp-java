package com.example.demo.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginPageController {

    @FXML
    private TextField cpfTextField;
    @FXML
    private TextField passwordTextField;

    //handler for enter pressed

    public void handleKeyPressed(KeyEvent keyEvent) {

        if(keyEvent.getCode().toString().equals("ENTER")){
            handleLogin();
        }
    }

    public void handleSubmitButton(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            handleLogin(); // Assuming this is your login logic method
        }
    }

    //handle login

    public void handleLogin(){
        String cpf = cpfTextField.getText();
        String password = passwordTextField.getText();

        if(!cpf.isEmpty() && password.isEmpty() ){
            if(validateCpf(cpf)){
                System.out.println("CPF válido: " + cpf);
                cpfTextField.setEditable(false);
            } else {
                System.out.println("CPF inválido: " + cpf);
                //show error message
            }
        }
        if(!cpf.isEmpty() && !password.isEmpty()){
            if(validateCpf(cpf) && validadePassword(password)){
                System.out.println("CPF e senha válidos: " + cpf + ", " + password);
                //proceed to next page
                try {

                    Connection userDBConnection = new UserConnection().conectar();
                    UserDAOs userDAOs = new UserDAOs();

                    User user = userDAOs.getUserByCpf(userDBConnection, cpf);

                    if (user == null) {
                        System.out.println("Usuário não encontrado.");
                        //show error message
                        return;
                    }

                    //add check to see if password is correct

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
                    Parent root = loader.load();

                    // get current stage from any node
                    Stage stage = (Stage) cpfTextField.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("CPF ou senha inválidos: " + cpf + ", " + password);
                //show error message
            }
        }
        if (cpf.isEmpty()){
            System.out.println("CPF inválido: " + cpf);
            //show error message
        }
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
