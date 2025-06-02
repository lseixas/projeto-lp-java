package com.example.demo.controllers;

import java.io.IOException;
import java.net.URL; // Importe URL
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle; // Importe ResourceBundle
import java.util.regex.Pattern;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;

import com.example.demo.util.Global;
import com.example.demo.util.PasswordHasher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Importe Initializable
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Importe Button se for referenciar resetButton
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginPageController implements Initializable { // Modificado aqui

    @FXML
    private TextField cpfTextField;
    @FXML
    private Label lAccCpfError;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label lAccPasswordError;

    private final String originalCpfTextFieldStyle = "-fx-background-color: transparent; -fx-text-fill: #173B64;";

    private String originalCpfHBoxStyle;
    private String lockedCpfHBoxStyle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Node cpfParentNode = cpfTextField.getParent();
        if (cpfParentNode instanceof HBox) {
            originalCpfHBoxStyle = cpfParentNode.getStyle();

            if (originalCpfHBoxStyle != null && originalCpfHBoxStyle.contains("-fx-background-color: white;")) {
                lockedCpfHBoxStyle = originalCpfHBoxStyle.replace(
                        "-fx-background-color: white;",
                        "-fx-background-color: #cccccc;"
                );
            } else {
                System.err.println("Estilo original da HBox do CPF não continha '-fx-background-color: white;'. Usando fallback para lockedHBoxStyle.");
                lockedCpfHBoxStyle = "-fx-background-color: #cccccc; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-color: #A3C4EB; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 25; " +
                        "-fx-padding: 5 10 5 10;";
                if (originalCpfHBoxStyle == null) {
                    originalCpfHBoxStyle = "-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #A3C4EB; -fx-border-width: 1.5px; -fx-border-radius: 25; -fx-padding: 5 10 5 10;";
                }
            }
        } else {
            System.err.println("O pai do cpfTextField não é uma HBox como esperado! Não será possível estilizar a HBox.");
            originalCpfHBoxStyle = "-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #A3C4EB; -fx-border-width: 1.5px; -fx-border-radius: 25; -fx-padding: 5 10 5 10;";
            lockedCpfHBoxStyle = originalCpfHBoxStyle.replace("-fx-background-color: white;", "-fx-background-color: #cccccc;");
        }


        // Listener do cpfTextField
        cpfTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                boolean isPotentialFullFormat = newValue.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

                if (isPotentialFullFormat && cpfTextField.isEditable()) {
                    try {
                        boolean isCpfActuallyValid = validateCpfField(cpfTextField, lAccCpfError);

                        if (isCpfActuallyValid) {
                            // CPF VÁLIDO!
                            Node parentNode = cpfTextField.getParent();
                            if (parentNode instanceof HBox && lockedCpfHBoxStyle != null) {
                                parentNode.setStyle(lockedCpfHBoxStyle);
                            }

                            // 2. Torna o TextField não editável
                            cpfTextField.setEditable(false);

                            // 3. Move o foco e tenta o login (auto tab)
                            passwordTextField.requestFocus();
                            handleLogin();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        showError(lAccCpfError, "Erro de DB ao validar CPF: " + e.getMessage());
                        // Reverte o estilo da HBox e a editabilidade do TextField em caso de erro durante a validação.
                        Node parentNode = cpfTextField.getParent();
                        if (parentNode instanceof HBox && originalCpfHBoxStyle != null) {
                            parentNode.setStyle(originalCpfHBoxStyle);
                        }
                        cpfTextField.setStyle(originalCpfTextFieldStyle); // Reseta o estilo do próprio TextField
                        cpfTextField.setEditable(true);
                    }
                }
            }
        });
    }

    @FXML
    public void handleResetButton(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            Node parentNode = cpfTextField.getParent();
            if (parentNode instanceof HBox && originalCpfHBoxStyle != null) {
                parentNode.setStyle(originalCpfHBoxStyle);
            }

            cpfTextField.clear();
            cpfTextField.setStyle(originalCpfTextFieldStyle); // Restaura estilo original do TextField
            cpfTextField.setEditable(true);

            passwordTextField.clear();
            hideError(lAccCpfError);
            hideError(lAccPasswordError);

            // 4. Foco no CPF
            cpfTextField.requestFocus();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) throws SQLException {

        if(keyEvent.getCode().toString().equals("ENTER")){
            handleLogin();
        }
    }

    public void handleSubmitButton(MouseEvent mouseEvent) throws SQLException {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            handleLogin();
        }
    }


    public void handleLogin() throws SQLException {
        String cpf = cpfTextField.getText();
        String password = passwordTextField.getText().trim();

        if(!cpf.isEmpty() && password.isEmpty() ){
            if(validateCpfField(cpfTextField, lAccCpfError)){
                System.out.println("CPF válido (chamado de handleLogin - senha vazia): " + cpf);
            } else {
                System.out.println("CPF inválido (chamado de handleLogin - senha vazia): " + cpf);
                // Mensagem de erro já é mostrada por validateCpfField
            }
        }
        // Se CPF e senha estão preenchidos (login normal)
        else if(!cpf.isEmpty() && !password.isEmpty()){
            boolean isCpfValid = validateCpfField(cpfTextField, lAccCpfError);

            if(isCpfValid && validatePasswordField(passwordTextField, lAccPasswordError)) {
                System.out.println("CPF e senha parecem válidos para tentativa de login: " + cpf + ", " + password);

                try {
                    Connection userDBConnection = new UserConnection().conectar();
                    UserDAOs userDAOs = new UserDAOs();
                    User user = userDAOs.getUserByCpf(userDBConnection, cpf);

                    if (user == null) {
                        System.out.println("Usuário não encontrado no DB.");
                        showError(lAccCpfError, "Senha ou cpf incorretos");
                        showError(lAccPasswordError, "Senha ou cpf incorretos");
                        return;
                    }

                    String hashedPassword = PasswordHasher.hash(password);

                    if (!user.getSenha().equals(hashedPassword)) {
                        System.out.println("Senha incorreta para o usuário: " + cpf);
                        showError(lAccPasswordError, "Senha ou cpf incorretos");
                        return;
                    }

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
                    Parent root = loader.load();

                    Global.setLoggedInUser(cpf); // Define o usuário logado globalmente

                    Stage stage = (Stage) cpfTextField.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException | SQLException e) {
                    showError(lAccCpfError, "Erro no sistema. Tente mais tarde.");
                    e.printStackTrace(); // Para debug no console
                }
            } else {
                System.out.println("CPF ou senha inválidos na tentativa de login: " + cpf + ", " + password);
            }
        }

        // Se CPF está vazio (validação primária)
        else if (cpf.isEmpty()){
            System.out.println("CPF vazio.");
            showError(lAccCpfError, "CPF é obrigatório.");
        }
    }

    public void handleCreateAccount(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/createAccountPage-view.fxml"));
                Parent root = loader.load();

                CreateAccountPageController createAccountPageController = loader.getController();
                createAccountPageController.initData(cpfTextField.getText());

                Stage stage = (Stage) cpfTextField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError(lAccCpfError, "Erro ao abrir tela de criação de conta.");
            }
        }
    }


    private boolean validateCpfField(TextField field, Label errorLabel) throws SQLException {
        if (validateNullField(field, errorLabel, "CPF é obrigatório.")) { // Verifica se está vazio
            String cpf = field.getText().trim();
            if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) { // Verifica o formato
                showError(errorLabel, "Formato do CPF inválido (XXX.XXX.XXX-XX).");
                return false;
            }

            Connection userDbConnection = null;
            try {
                userDbConnection = new UserConnection().conectar();
                UserDAOs userDAOs = new UserDAOs();

                if (userDAOs.getUserByCpf(userDbConnection, cpf) != null) {
                    hideError(errorLabel);
                    return true;
                } else {
                    showError(errorLabel, "CPF não cadastrado.");
                    return false;
                }
            } finally {
                // Garante que a conexão seja fechada mesmo se ocorrer uma exceção
                if (userDbConnection != null) {
                    try {
                        userDbConnection.close();
                    } catch (SQLException e) {
                        e.printStackTrace(); // Logar erro no fechamento da conexão
                    }
                }
            }
        }
        return false;
    }

    private boolean validatePasswordField(PasswordField field, Label errorLabel) {
        if (validateNullField(field, errorLabel, "Senha é obrigatória.")) {
            String password = field.getText().trim();
            if (password.length() < 6) { // Verifica o tamanho mínimo
                showError(errorLabel, "A senha deve ter pelo menos 6 caracteres.");
                return false;
            } else {
                hideError(errorLabel);
                return true;
            }
        }
        return false;
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

    private boolean validateNullField(TextField field, Label errorLabel, String errorMessage) {
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
            errorLabel.setManaged(true);
        }
    }

    private void hideError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
    }

}