package com.example.demo.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;

import com.example.demo.util.Global;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent; // Import if using PasswordField
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
    @FXML private Label cAccBirthError; // Error label for birthdate

    @FXML public TextField cAccCpfField;
    @FXML private Label cAccCpfError; // Error label for CPF

    @FXML public PasswordField cAccPasswordField;
    @FXML private Label cAccPasswordError; // Error label for password

    @FXML public PasswordField cAccConfirmPasswordField;
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

        bindManagedToVisible(cAccNameError);
        bindManagedToVisible(cAccEmailError);
        bindManagedToVisible(cAccBirthError);
        bindManagedToVisible(cAccCpfError);
        bindManagedToVisible(cAccPasswordError);
        bindManagedToVisible(cAccConfirmPasswordError);

        cAccBirthField.setTextFormatter(new TextFormatter<>(birthDateFilter));
        cAccCpfField.setTextFormatter(new TextFormatter<>(cpfFilter));

    }

    //logic for birthdate formatting (listener)

    UnaryOperator<TextFormatter.Change> birthDateFilter = change -> {
        String currentTextInField = ((TextInputControl) change.getControl()).getText();
        String proposedTextIfNoFormatting = change.getControlNewText();

        String digitsOnly = proposedTextIfNoFormatting.replaceAll("[^\\d]", "");

        if (digitsOnly.length() > 8) {
            digitsOnly = digitsOnly.substring(0, 8); // Limit to 8 digits (DDMMYYYY)
        }

        StringBuilder idealFormattedTextBuilder = new StringBuilder();
        int len = digitsOnly.length();

        if (len > 0) idealFormattedTextBuilder.append(digitsOnly.substring(0, Math.min(len, 2))); // DD
        if (len >= 2) idealFormattedTextBuilder.append("/");                                       // DD/
        if (len > 2) idealFormattedTextBuilder.append(digitsOnly.substring(2, Math.min(len, 4)));// MM
        if (len >= 4) idealFormattedTextBuilder.append("/");                                       // DD/MM/
        if (len > 4) idealFormattedTextBuilder.append(digitsOnly.substring(4, Math.min(len, 8)));// YYYY

        String idealFormattedText = idealFormattedTextBuilder.toString();
        String textToSet = idealFormattedText; // By default, use the fully formatted version
        int newCaretPosition = idealFormattedText.length(); // Default caret to end

        //logic for back-space pressed

        if (change.isDeleted()) {
            if (idealFormattedText.endsWith("/") &&
                    !proposedTextIfNoFormatting.endsWith("/") && // Ensure the proposed text is "clean"
                    proposedTextIfNoFormatting.equals(idealFormattedText.substring(0, idealFormattedText.length() - 1))) {

                textToSet = proposedTextIfNoFormatting;
                newCaretPosition = textToSet.length();
            } else {
                newCaretPosition = Math.min(change.getRangeStart(), textToSet.length());
            }
        } else if (change.isAdded()) {
            if (len == 2 && textToSet.endsWith("/")) newCaretPosition = 3;       // After DD/
            else if (len == 4 && textToSet.endsWith("/")) newCaretPosition = 6;  // After DD/MM/
            else if (len == 8 && textToSet.length() == 10) newCaretPosition = 10; // End of DD/MM/YYYY
        }


        change.setText(textToSet);
        change.setRange(0, currentTextInField.length()); // Replace the entire current text
        change.selectRange(newCaretPosition, newCaretPosition); // Set the new caret position

        return change;
    };

    //logic for CPF formatting (listener)

    UnaryOperator<TextFormatter.Change> cpfFilter = change -> {
        String currentTextInField = ((TextInputControl) change.getControl()).getText();
        String proposedTextIfNoFormatting = change.getControlNewText(); // What TextField thinks text will be

        // --- Debugging Output - VERY IMPORTANT! ---
        System.out.println("--- CPF Filter ---");
        System.out.println("isAdded: " + change.isAdded() + ", isDeleted: " + change.isDeleted() + ", isReplaced: " + change.isReplaced());
        System.out.println("Range: " + change.getRangeStart() + "-" + change.getRangeEnd() + ", Text: '" + change.getText() + "'");
        System.out.println("Caret Pos: " + change.getCaretPosition() + ", Anchor: " + change.getAnchor());
        System.out.println("Current Field Text: '" + currentTextInField + "'");
        System.out.println("Proposed Text If No Formatting: '" + proposedTextIfNoFormatting + "'");
        // --- End Debugging Output ---

        String digitsOnly = proposedTextIfNoFormatting.replaceAll("[^\\d]", "");

        if (digitsOnly.length() > 11) {
            digitsOnly = digitsOnly.substring(0, 11);
        }

        StringBuilder idealFormattedTextBuilder = new StringBuilder();
        int len = digitsOnly.length();

        // Build the ideal formatted string based on digitsOnly
        if (len > 0) idealFormattedTextBuilder.append(digitsOnly.substring(0, Math.min(len, 3)));
        if (len >= 3) {
            idealFormattedTextBuilder.append(".");
            if (len > 3) idealFormattedTextBuilder.append(digitsOnly.substring(3, Math.min(len, 6)));
        }
        if (len >= 6) {
            idealFormattedTextBuilder.append(".");
            if (len > 6) idealFormattedTextBuilder.append(digitsOnly.substring(6, Math.min(len, 9)));
        }
        if (len >= 9) {
            idealFormattedTextBuilder.append("-");
            if (len > 9) idealFormattedTextBuilder.append(digitsOnly.substring(9, Math.min(len, 11)));
        }

        String idealFormattedText = idealFormattedTextBuilder.toString();
        String textToSet = idealFormattedText;
        int newCaretPosition = idealFormattedText.length();

        //logic for back-space pressed

        if (!change.isAdded() && !change.isDeleted() && !change.isReplaced() &&
                change.getText().isEmpty() &&
                currentTextInField.equals(proposedTextIfNoFormatting) &&
                change.getRangeStart() == currentTextInField.length() &&
                currentTextInField.length() > 0) {

            char lastCharOfCurrentText = currentTextInField.charAt(currentTextInField.length() - 1);
            int numDigitsInCurrentText = currentTextInField.replaceAll("[^\\d]","").length();

            boolean wasTrailingSeparatorWeCareAbout = false;
            if (lastCharOfCurrentText == '.' && (numDigitsInCurrentText == 3 || numDigitsInCurrentText == 6)) {
                wasTrailingSeparatorWeCareAbout = true;
            } else if (lastCharOfCurrentText == '-' && numDigitsInCurrentText == 9) {
                wasTrailingSeparatorWeCareAbout = true;
            }

            if (wasTrailingSeparatorWeCareAbout) {
                textToSet = currentTextInField.substring(0, currentTextInField.length() - 1);
                newCaretPosition = textToSet.length();
                System.out.println("  FORCED separator deletion. New textToSet: '" + textToSet + "'");
            }
        } else if (change.isDeleted()) {
            newCaretPosition = Math.min(change.getRangeStart(), textToSet.length());
            System.out.println("  Standard deletion (digit?). Caret at: " + newCaretPosition);
        } else if (change.isAdded()) {
            System.out.println("  Addition. Caret at: " + newCaretPosition);
        }

        change.setText(textToSet);
        change.setRange(0, currentTextInField.length()); // Replace the entire current text
        change.selectRange(newCaretPosition, newCaretPosition); // Set the new caret position

        System.out.println("Final text set: '" + textToSet + "', caret: " + newCaretPosition);
        System.out.println("--- CPF Filter End ---");
        return change;
    };

    private void bindManagedToVisible(Label label) {
        if (label != null) {
            label.managedProperty().bind(label.visibleProperty());
        }
    }

    public void handleEnterPressed(KeyEvent keyEvent) throws SQLException, IOException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            System.out.println("Enter key pressed");
            validateAndProceed();
        }
    }

    @FXML // Add this if you added the button in FXML
    private void submitForm(ActionEvent event) throws SQLException, IOException {
        System.out.println("Submit button pressed");
        validateAndProceed();
    }

    private void validateAndProceed() throws SQLException, IOException {
        boolean allFieldsValid = true;

        // Validate each field
        allFieldsValid &= validateNameField(cAccNameField, cAccNameError);
        allFieldsValid &= validateEmailField(cAccEmailField, cAccEmailError);
        allFieldsValid &= validateBirthDateField(cAccBirthField, cAccBirthError);
        allFieldsValid &= validateCpfField(cAccCpfField, cAccCpfError);
        allFieldsValid &= validatePasswordField(cAccPasswordField, cAccPasswordError);
        allFieldsValid &= validateConfirmPasswordField(cAccConfirmPasswordField, cAccConfirmPasswordError);

        if (allFieldsValid) {
            System.out.println("Formulário válido. Prosseguindo com a criação da conta...");

            if (handleLogin() != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
                Parent root = loader.load();

                Global.setLoggedInUser(cAccCpfField.getText().trim());

                Stage stage = (Stage) cAccCpfField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                System.out.println("Erro ao criar usuário. Verifique os dados e tente novamente.");
            };
            clearAllFieldsAndErrors();
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

    private boolean validateEmailField(TextField field, Label errorLabel) throws SQLException {
        if (validateNullField(field, cAccEmailError, "Email é obrigatório.")) {
            String email = field.getText().trim();
            System.out.println(cAccEmailField.getText());
            if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
                showError(errorLabel, "Email inválido.");
                return false;
            }
            Connection userDbConnection = new UserConnection().conectar();
            UserDAOs userDAOs = new UserDAOs();
            if (!(userDAOs.getUserByEmail(userDbConnection, field.getText().trim()) == null)) {
                showError(errorLabel, "Email já cadastrado.");
                return false;
            }
            else {
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
        return false;
    }

    private boolean validateCpfField(TextField field, Label errorLabel) throws SQLException {
        if(validateNullField(field, errorLabel, "CPF é obrigatório.")) {
            String cpf = field.getText().trim();
            if (!cpf.matches("\\d{3}.\\d{3}.\\d{3}-\\d{2}")) { // CPF must be 11 digits
                showError(errorLabel, "CPF inválido.");
                return false;
            }
            Connection userDbConnection = new UserConnection().conectar();
            UserDAOs userDAOs = new UserDAOs();
            if (!(userDAOs.getUserByCpf(userDbConnection, field.getText().trim()) == null)) {
                showError(errorLabel, "CPF já cadastrado.");
                return false;
            }
            else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
    }

    private boolean validatePasswordField(PasswordField field, Label errorLabel) {
        if (validateNullField(field, cAccPasswordError, "Senha é obrigatória.")) {
            String password = field.getText().trim();
            if (password.length() < 8) {
                showError(errorLabel, "A senha deve ter pelo menos 6 caracteres.");
                return false;
            } else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
    }

    private boolean validateConfirmPasswordField(PasswordField field, Label errorLabel) {
        if (validateNullField(field, errorLabel, "Confirmação de senha é obrigatória.")) {
            String confirmPassword = field.getText().trim();
            if (confirmPassword.length() < 8) {
                showError(errorLabel, "A confirmação de senha deve ter pelo menos 6 caracteres.");
                return false;
            }
            else if (!cAccPasswordField.getText().trim().equals(confirmPassword)) {
                showError(errorLabel, "As senhas não coincidem.");
                return false;
            }
            else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
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


    public User handleLogin() throws SQLException {
        Connection userDbConnection = new UserConnection().conectar();
        UserDAOs userDAOs = new UserDAOs();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localBirthDate = LocalDate.parse(cAccBirthField.getCharacters(), inputFormatter);

        User NewUser = new User(
            cAccNameField.getText().trim(),
            cAccEmailField.getText().trim(),
            cAccCpfField.getText().trim(),
            cAccPasswordField.getText().trim(),
            0.0f, // Default balance
            java.sql.Date.valueOf(localBirthDate)
        );

        System.out.println(NewUser.toString());

        try {
            User createdUser = userDAOs.createUser(userDbConnection, NewUser);
            if(createdUser != null) {
                System.out.println("Usuário criado com sucesso: " + createdUser);
                return createdUser; // Return the created user
            } else {
                System.out.println("Falha ao criar usuário.");
                return null;
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Erro ao criar usuário: " + e.getMessage());
            return null;
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