package com.example.demo.controllers;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;
import com.example.demo.util.Global;
import com.example.demo.util.PasswordHasher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateAccountPageController extends JFrame {

    private JTextField nameField, emailField, birthField, cpfField;
    private JPasswordField passwordField, confirmPasswordField;
    private JLabel nameError, emailError, birthError, cpfError, passwordError, confirmError;

    public CreateAccountPageController() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("🧾 Criar Conta");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Nome
        gbc.gridy++;
        add(new JLabel("👨‍💼 Nome Completo:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        nameError = errorLabel();
        gbc.gridwidth = 2;
        add(nameError, gbc);
        gbc.gridwidth = 1;

        // Email
        gbc.gridy++;
        add(new JLabel("📧 Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        emailError = errorLabel();
        gbc.gridwidth = 2;
        add(emailError, gbc);
        gbc.gridwidth = 1;

        // Data de Nascimento
        gbc.gridy++;
        add(new JLabel("📅 Data de Nascimento (DD/MM/YYYY):"), gbc);
        birthField = new JTextField(20);
        gbc.gridx = 1;
        add(birthField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        birthError = errorLabel();
        gbc.gridwidth = 2;
        add(birthError, gbc);
        gbc.gridwidth = 1;

        // CPF
        gbc.gridy++;
        add(new JLabel("🆔 CPF:"), gbc);
        cpfField = new JTextField(20);
        gbc.gridx = 1;
        add(cpfField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        cpfError = errorLabel();
        gbc.gridwidth = 2;
        add(cpfError, gbc);
        gbc.gridwidth = 1;

        // Senha
        gbc.gridy++;
        add(new JLabel("🔐 Senha:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        passwordError = errorLabel();
        gbc.gridwidth = 2;
        add(passwordError, gbc);
        gbc.gridwidth = 1;

        // Confirmar Senha
        gbc.gridy++;
        add(new JLabel("🔐 Confirmar Senha:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        confirmError = errorLabel();
        gbc.gridwidth = 2;
        add(confirmError, gbc);
        gbc.gridwidth = 1;

        // Botão Criar Conta
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton createButton = new JButton("✅ Criar Conta");
        createButton.addActionListener(this::handleSubmit);
        add(createButton, gbc);

        // Link Login
        gbc.gridy++;
        JButton loginButton = new JButton("Já tenho uma conta");
        loginButton.setBorderPainted(false);
        loginButton.setForeground(Color.BLUE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> switchToLogin());
        add(loginButton, gbc);
    }

    // === Lógica principal ===
    private void handleSubmit(ActionEvent e) {
        clearErrors();
        boolean valid = validateFields();
    
        if (!valid) return;
    
        try {
            User created = createUser();
            if (created != null) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Define o usuário logado globalmente
                Global.setLoggedInUser(cpfField.getText().trim());
    
                // Abre a MainPage
                java.awt.EventQueue.invokeLater(() -> {
                    MainPageController mainPage = new MainPageController(); // sua JFrame da MainPage
                    mainPage.setVisible(true);
                });
    
                // Fecha a tela de cadastro atual
                this.dispose();
    
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar conta. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // === Validação de campos ===
    private boolean validateFields() {
        boolean ok = true;

        if (nameField.getText().trim().isEmpty()) {
            showError(nameError, "Nome é obrigatório.");
            ok = false;
        } else if (nameField.getText().trim().length() < 3) {
            showError(nameError, "O nome deve ter pelo menos 3 letras.");
            ok = false;
        }

        if (emailField.getText().trim().isEmpty()) {
            showError(emailError, "Email é obrigatório.");
            ok = false;
        } else if (!emailField.getText().matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            showError(emailError, "Email inválido.");
            ok = false;
        }

        if (!birthField.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
            showError(birthError, "Data inválida. Use DD/MM/YYYY.");
            ok = false;
        }

        if (!cpfField.getText().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            showError(cpfError, "CPF inválido. Use 999.999.999-99.");
            ok = false;
        }

        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        if (pass.length() < 6) {
            showError(passwordError, "Senha deve ter pelo menos 6 caracteres.");
            ok = false;
        }
        if (!pass.equals(confirm)) {
            showError(confirmError, "As senhas não coincidem.");
            ok = false;
        }

        return ok;
    }

    private User createUser() throws SQLException {
        Connection conn = new UserConnection().conectar();
        UserDAOs dao = new UserDAOs();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birth = LocalDate.parse(birthField.getText().trim(), fmt);
        String hashed = PasswordHasher.hash(new String(passwordField.getPassword()));

        User user = new User(
                nameField.getText().trim(),
                emailField.getText().trim(),
                cpfField.getText().trim(),
                hashed,
                0.0f,
                java.sql.Date.valueOf(birth)
        );

        return dao.createUser(conn, user);
    }

    // === Navegação ===
    private void switchToLogin() {
        new LoginPageController();
    }

    // === Helpers ===
    private JLabel errorLabel() {
        JLabel label = new JLabel("");
        label.setForeground(Color.RED);
        return label;
    }

    private void showError(JLabel label, String msg) {
        label.setText(msg);
    }

    private void clearErrors() {
        for (JLabel l : new JLabel[]{nameError, emailError, birthError, cpfError, passwordError, confirmError}) {
            l.setText("");
        }
    }
}