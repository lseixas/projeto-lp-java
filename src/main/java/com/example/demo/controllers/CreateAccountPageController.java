package com.example.demo.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User; // Import Label

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent; // Import if using PasswordField
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField; // Import for button action
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

        cAccBirthField.textProperty().addListener(new ChangeListener<String>() {
            private boolean K_applying_format_X = false; // Flag to prevent listener recursion

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (K_applying_format_X) {
                    return; // Exit if the change was made by this listener
                }

                K_applying_format_X = true; // Set flag to indicate programmatic change

                // 1. Remove all non-digit characters to get a clean sequence of numbers
                String digitsOnly = newValue.replaceAll("[^\\d]", "");

                StringBuilder formattedText = new StringBuilder();
                int len = digitsOnly.length();

                // 2. Build the formatted text DD/MM/YYYY
                if (len > 0) {
                    formattedText.append(digitsOnly.substring(0, Math.min(len, 2))); // Day part (DD)
                }
                if (len > 2) {
                    formattedText.append("/"); // Add slash after day
                    formattedText.append(digitsOnly.substring(2, Math.min(len, 4))); // Month part (MM)
                }
                if (len > 4) {
                    formattedText.append("/"); // Add slash after month
                    formattedText.append(digitsOnly.substring(4, Math.min(len, 8))); // Year part (YYYY)
                }

                // 3. Limit total length to DD/MM/YYYY (10 characters)
                if (formattedText.length() > 10) {
                    formattedText.setLength(10);
                }

                String finalText = formattedText.toString();

                // 4. Update the TextField only if the formatted text is different
                //    This helps manage cursor position and avoid unnecessary updates.
                if (!newValue.equals(finalText)) {
                    cAccBirthField.setText(finalText);
                    // For simplicity, move the caret (cursor) to the end of the text.
                    // More sophisticated cursor management is possible but adds complexity.
                    cAccBirthField.positionCaret(finalText.length());
                }

                K_applying_format_X = false; // Reset flag
            }
        });
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
        allFieldsValid &= validateNameField(cAccNameField, cAccNameError);
        allFieldsValid &= validateEmailField(cAccEmailField, cAccEmailError);
        allFieldsValid &= validateBirthDateField(cAccBirthField, cAccBirthError);
        allFieldsValid &= validateNullField(cAccCpfField, cAccCpfError, "CPF é obrigatório.");
        allFieldsValid &= validateNullField(cAccPasswordField, cAccPasswordError, "Senha é obrigatória.");
        allFieldsValid &= validateNullField(cAccConfirmPasswordField, cAccConfirmPasswordError, "Confirmação de senha é obrigatória.");

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

    private boolean validateNameField(TextField field, Label errorLabel) {
        if (validateNullField(field, cAccNameError, "Nome completo é obrigatório.")) {
            String name = field.getText().trim();
            if (name.length() < 3) {
                showError(errorLabel, "O nome deve ter pelo menos 3 caracteres.");
                return false;
            } else if (!name.matches("[a-zA-Z\\s]+")) {
                showError(errorLabel, "O nome deve conter apenas letras e espaços.");
                return false;
            } else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
    }

    private boolean validateEmailField(TextField field, Label errorLabel) {
        if (validateNullField(field, cAccEmailError, "Email é obrigatório.")) {
            String email = field.getText().trim();
            System.out.println(cAccEmailField.getText());
            if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
                showError(errorLabel, "Email inválido.");
                return false;
            } else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
    }

    private boolean validateBirthDateField(TextField field, Label errorLabel) {
        if (validateNullField(field, errorLabel, "Data de nascimento é obrigatória.")) { // Pass the correct errorLabel
            String birthDate = field.getText().trim();
            if (!birthDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                showError(errorLabel, "Data inválida. Use DD/MM/YYYY e certifique-se que a data é completa.");
                return false;
            } else {
                hideError(errorLabel);
                return true;
            }
        }
        return false; // This is returned if validateNullField is false
    }

    private boolean validateNullField(TextField field, Label errorLabel, String errorMessage) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    private boolean validateNullField(PasswordField field, Label errorLabel, String errorMessage) {
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

    public void handleLoginLinkAction(MouseEvent mouseEvent) throws SQLException, IOException {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/loginPage-view.fxml"));
            Parent root = loader.load();

            // get current stage from any node
            Stage stage = (Stage) cAccCpfField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}