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
        // Captura o nó pai (HBox) do cpfTextField
        Node cpfParentNode = cpfTextField.getParent();
        if (cpfParentNode instanceof HBox) {
            // 1. Armazena o estilo original da HBox (definido no FXML)
            originalCpfHBoxStyle = cpfParentNode.getStyle();

            // 2. Cria o estilo "bloqueado" para a HBox
            // Tentamos substituir apenas a cor de fundo do estilo original.
            // Se o estilo original não for encontrado como esperado, usamos um fallback.
            if (originalCpfHBoxStyle != null && originalCpfHBoxStyle.contains("-fx-background-color: white;")) {
                lockedCpfHBoxStyle = originalCpfHBoxStyle.replace(
                        "-fx-background-color: white;",
                        "-fx-background-color: #cccccc;" // Cinza claro. Mude para um tom mais escuro se preferir (ex: #b0b0b0)
                );
            } else {
                // Fallback: Define um estilo completo se o original não puder ser processado
                // Certifique-se que este fallback corresponda às outras propriedades do FXML (radius, border, padding)
                System.err.println("Estilo original da HBox do CPF não continha '-fx-background-color: white;'. Usando fallback para lockedHBoxStyle.");
                lockedCpfHBoxStyle = "-fx-background-color: #cccccc; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-color: #A3C4EB; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 25; " + // No seu FXML original, -fx-border-radius aparece duas vezes.
                        "-fx-padding: 5 10 5 10;";
                // Se o originalCpfHBoxStyle for nulo, também defina um fallback para ele para o reset
                if (originalCpfHBoxStyle == null) {
                    originalCpfHBoxStyle = "-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #A3C4EB; -fx-border-width: 1.5px; -fx-border-radius: 25; -fx-padding: 5 10 5 10;";
                }
            }
        } else {
            System.err.println("O pai do cpfTextField não é uma HBox como esperado! Não será possível estilizar a HBox.");
            // Defina fallbacks para os estilos para evitar NullPointerExceptions se o pai não for uma HBox
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
                            // 1. Modifica o estilo da HBox PAI
                            Node parentNode = cpfTextField.getParent();
                            if (parentNode instanceof HBox && lockedCpfHBoxStyle != null) {
                                parentNode.setStyle(lockedCpfHBoxStyle);
                            }

                            // 2. Torna o TextField não editável (isso continua)
                            cpfTextField.setEditable(false);

                            // 3. Move o foco e tenta o login
                            passwordTextField.requestFocus();
                            handleLogin();
                        }
                        // Se isCpfActuallyValid for false, validateCpfField já mostrou o erro.
                        // A HBox e o TextField não são alterados.

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

    // ... (SEU MÉTODO validateCpfField e outros métodos como handleLogin, showError, hideError) ...
    // Cole aqui a versão do validateCpfField que retorna true se o CPF for válido para login
    // (formato OK e usuário existe no DB), e que fecha a conexão com o banco.

    // Método para o botão Resetar ATUALIZADO

    @FXML
    public void handleResetButton(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            // 1. Restaura o estilo original da HBox PAI
            Node parentNode = cpfTextField.getParent();
            if (parentNode instanceof HBox && originalCpfHBoxStyle != null) {
                parentNode.setStyle(originalCpfHBoxStyle);
            }

            // 2. Limpa e restaura o TextField
            cpfTextField.clear();
            cpfTextField.setStyle(originalCpfTextFieldStyle); // Restaura estilo original do TextField
            cpfTextField.setEditable(true);

            // 3. Limpa campo de senha e erros
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
        String password = passwordTextField.getText();

        // Se o CPF está preenchido e a senha vazia (pode ser o caso do auto-trigger)
        if(!cpf.isEmpty() && password.isEmpty() ){
            if(validateCpfField(cpfTextField, lAccCpfError)){ // Valida CPF (e já define como não editável se passar)
                System.out.println("CPF válido (chamado de handleLogin - senha vazia): " + cpf);
                // cpfTextField.setEditable(false); // O listener já faz isso, mas pode ficar por redundância segura
            } else {
                System.out.println("CPF inválido (chamado de handleLogin - senha vazia): " + cpf);
                // Mensagem de erro já é mostrada por validateCpfField
            }
        }
        // Se CPF e senha estão preenchidos (login normal)
        else if(!cpf.isEmpty() && !password.isEmpty()){
            // Garante que o CPF ainda seja considerado válido aqui também
            boolean isCpfValid = validateCpfField(cpfTextField, lAccCpfError);

            if(isCpfValid && validatePasswordField(passwordTextField, lAccPasswordError)) {
                System.out.println("CPF e senha parecem válidos para tentativa de login: " + cpf + ", " + password);

                try {
                    Connection userDBConnection = new UserConnection().conectar();
                    UserDAOs userDAOs = new UserDAOs();
                    User user = userDAOs.getUserByCpf(userDBConnection, cpf);

                    if (user == null) {
                        System.out.println("Usuário não encontrado no DB.");
                        showError(lAccCpfError, "Senha ou cpf incorretos"); // Mostra no CPF, poderia ser um label geral
                        showError(lAccPasswordError, "Senha ou cpf incorretos");
                        return;
                    }

                    // AQUI IRIA A LÓGICA DE VERIFICAÇÃO DA SENHA (user.getPassword().equals(password))
                    // Por enquanto, se o usuário existe, assume-se que o login é bem-sucedido para navegação

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/views/mainPage-view.fxml"));
                    Parent root = loader.load();
                    MainPageController mainPageController = loader.getController();
                    System.out.println("Usuário encontrado no DB: " + user.getNome());
                    mainPageController.initData(user);

                    Stage stage = (Stage) cpfTextField.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException | SQLException e) {
                    // Em caso de erro de IO ou SQL, podemos mostrar uma mensagem mais genérica
                    showError(lAccCpfError, "Erro no sistema. Tente mais tarde.");
                    e.printStackTrace(); // Para debug no console
                    // throw new RuntimeException(e); // Evite RuntimeException não tratada se possível
                }
            } else {
                System.out.println("CPF ou senha inválidos na tentativa de login: " + cpf + ", " + password);
                // As mensagens de erro já devem ter sido mostradas por validateCpfField/validatePasswordField
            }
        }
        // Se CPF está vazio (validação primária)
        else if (cpf.isEmpty()){ // else if para garantir que não entre aqui se cpf e senha estiverem preenchidos mas vazios
            System.out.println("CPF vazio.");
            showError(lAccCpfError, "CPF é obrigatório.");
        }
        // Se a senha estiver vazia mas o CPF não (e não caiu no primeiro if - pode ser redundante)
        else if (password.isEmpty() && !cpf.isEmpty()) {
            showError(lAccPasswordError, "Senha é obrigatória.");
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
                // Melhor tratar a exceção ou logar do que apenas lançar RuntimeException
                e.printStackTrace();
                showError(lAccCpfError, "Erro ao abrir tela de criação de conta.");
                // throw new RuntimeException(e);
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

            Connection userDbConnection = null; // Declare a conexão fora do try para o finally
            try {
                userDbConnection = new UserConnection().conectar();
                UserDAOs userDAOs = new UserDAOs();

                if (userDAOs.getUserByCpf(userDbConnection, cpf) != null) { // Usuário EXISTE no DB
                    hideError(errorLabel); // Formato e existência OK, esconde erros prévios no campo CPF
                    return true;           // CPF VÁLIDO para login
                } else { // Usuário NÃO EXISTE no DB
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
        return false; // Retorna false se validateNullField falhar
    }

    private boolean validatePasswordField(PasswordField field, Label errorLabel) {
        if (validateNullField(field, errorLabel, "Senha é obrigatória.")) {
            String password = field.getText().trim();
            // A sua mensagem de erro diz 6, mas o código original tinha password.length() < 8
            if (password.length() < 6) { // Ajustado para 6 conforme sua mensagem de erro anterior
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
            errorLabel.setManaged(true); // Garanta que está aqui
        }
    }

    private void hideError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.setManaged(false); // Garanta que está aqui
        }
    }

    // Remova o método public void Initialize() se ele estiver vazio e não for o initialize do Initializable.
}