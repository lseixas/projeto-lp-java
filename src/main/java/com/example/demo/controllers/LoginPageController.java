package com.example.demo.controllers;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

    private boolean loadedPasswordField = false;

    //handler for enter pressed

    public void handleEnterPressed(){
        handleLogin();
    }

    //handler for button click

    public void handleLoginButtonClick(){
        handleLogin();
    }

    //handle login

    public void handleLogin(){
        String cpf = cpfTextField.getText();
        String password = passwordTextField.getText();

        if(!cpf.isEmpty() && password.isEmpty() ){
            if(validateCpf(cpf)){
                System.out.println("CPF válido: " + cpf);
                addExtraInputPane();
                cpfTextField.setEditable(false);
                loadedPasswordField = true;
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
