package com.example.demo.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User; // Import Label

import javafx.event.ActionEvent; // Import if using PasswordField
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField; // Import for button action
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CreateAccountPageController {

    @FXML public TextField cAccNameField;
    @FXML private Label cAccNameError; // Error label for name

    @FXML public TextField cAccEmailField;
    @FXML private Label cAccEmailError; // Error label for email

    @FXML public TextField cAccBirthField;
    @FXML private Label cAccBirthError; // Error label for birth date

    @FXML public TextField cAccCpfField;
    @FXML private Label cAccCpfError; // Error label for CPF

    @FXML public PasswordField cAccPasswordField; // Or PasswordField cAccPasswordField;
    @FXML private Label cAccPasswordError; // Error label for password

    @FXML public PasswordField cAccConfirmPasswordField; // Or PasswordField cAccConfirmPasswordField;
    @FXML private Label cAccConfirmPasswordError; // Error label for confirm password

    public void initData(String cpfFromLastPage){
        if (cAccCpfField.getText().isEmpty()) {
            cAccCpfField.setText(cpfFromLastPage);
            System.out.println("CPF from last page: " + cpfFromLastPage);
        } else {
            System.out.println("CPF field already filled: " + cAccCpfField.getText());
        }
    }

    public void initialize() {
        // Initialization logic if needed
        // Bind managed property to visible property for all error labels
        // This ensures they only take up space when visible.
        bindManagedToVisible(cAccNameError);
        bindManagedToVisible(cAccEmailError);
        bindManagedToVisible(cAccBirthError);
        bindManagedToVisible(cAccCpfError);
        bindManagedToVisible(cAccPasswordError);
        bindManagedToVisible(cAccConfirmPasswordError);
    }

    private void bindManagedToVisible(Label label) {
        if (label != null) {
            label.managedProperty().bind(label.visibleProperty());
        }
    }

    public void handleEnterPressed(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            System.out.println("Enter key pressed");
            validateAndProceed();
        }
    }

    @FXML // Add this if you added the button in FXML
    private void submitForm(ActionEvent event) throws SQLException {
        System.out.println("Submit button pressed");
        validateAndProceed();
    }

    private void validateAndProceed() throws SQLException {
        boolean allFieldsValid = true;

        // Validate each field
        allFieldsValid &= validateField(cAccNameField, cAccNameError, "Nome completo é obrigatório.");
        allFieldsValid &= validateField(cAccEmailField, cAccEmailError, "Email é obrigatório.");
        allFieldsValid &= validateField(cAccBirthField, cAccBirthError, "Data de nascimento é obrigatória.");
        allFieldsValid &= validateField(cAccCpfField, cAccCpfError, "CPF é obrigatório.");
        allFieldsValid &= validateField(cAccPasswordField, cAccPasswordError, "Senha é obrigatória.");
        allFieldsValid &= validateField(cAccConfirmPasswordField, cAccConfirmPasswordError, "Confirmação de senha é obrigatória.");

        // Additional check for password confirmation match, only if both fields are filled
        if (allFieldsValid && cAccPasswordField.getText() != null && !cAccPasswordField.getText().isEmpty() &&
                cAccConfirmPasswordField.getText() != null && !cAccConfirmPasswordField.getText().isEmpty()) {
            if (!cAccPasswordField.getText().equals(cAccConfirmPasswordField.getText())) {
                showError(cAccConfirmPasswordError, "As senhas não coincidem.");
                allFieldsValid = false;
            } else {
                // If they match and an error was previously shown for mismatch, hide it.
                // The emptiness check for cAccConfirmPasswordError is already handled by validateField.
                // This ensures only the mismatch error is cleared if passwords now match.
                if (cAccConfirmPasswordError.getText().equals("As senhas não coincidem.")) {
                    hideError(cAccConfirmPasswordError);
                }
            }
        }


        if (allFieldsValid) {
            // All fields are valid and passwords match (if applicable)
            System.out.println("Formulário válido. Prosseguindo com a criação da conta...");
            // Add your logic here for creating the account
            // For example, get text from fields:
            // String name = cAccNameField.getText();
            // String email = cAccEmailField.getText();
            // ... etc.
            handleLogin();
            clearAllFieldsAndErrors(); // Optionally clear fields after successful submission
        } else {
            System.out.println("Formulário inválido. Por favor, corrija os erros.");
        }
    }

    private boolean validateField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    private boolean validateField(PasswordField field, Label errorLabel, String errorMessage) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    private void showError(Label errorLabel, String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText(""); // Clear the text
            errorLabel.setVisible(false);
        }
    }

    private void clearAllFieldsAndErrors() {
        cAccNameField.clear();
        cAccEmailField.clear();
        cAccBirthField.clear();
        cAccCpfField.clear();
        cAccPasswordField.clear();
        cAccConfirmPasswordField.clear();

        hideError(cAccNameError);
        hideError(cAccEmailError);
        hideError(cAccBirthError);
        hideError(cAccCpfError);
        hideError(cAccPasswordError);
        hideError(cAccConfirmPasswordError);
    }


    public void handleLogin() throws SQLException {
        Connection userDbConnection = new UserConnection().conectar();
        UserDAOs userDAOs = new UserDAOs();

        User NewUser = new User(
                cAccNameField.getText(),
                cAccCpfField.getText(),
                cAccEmailField.getText(),
                cAccPasswordField.getText()
        );

        System.out.println(NewUser.toString());

        try {
            userDAOs.createUser(userDbConnection, NewUser);
            System.out.println("Usuário criado com sucesso!");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public void handleBackToLogin(ActionEvent actionEvent) {
    }

    public void handleLoginLinkAction(ActionEvent actionEvent) {
    }
}