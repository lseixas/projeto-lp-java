package com.example.demo.controllers;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;
import com.example.demo.util.Global;
import com.example.demo.util.PasswordHasher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class LoginPageController extends JFrame {

    private JTextField cpfField;
    private JPasswordField passwordField;
    private JLabel cpfErrorLabel;
    private JLabel passwordErrorLabel;
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton resetButton;
    private JButton exitButton;
    private Object cpfMask;
    
        public LoginPageController() {
            setTitle("Banco CVETTI - Login");
            setSize(450, 550);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setUndecorated(true); // deixa sem borda padrão
            setResizable(false);
            try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfMask);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Cria o campo formatado
            JFormattedTextField cpfField = new JFormattedTextField(cpfMask);
        cpfField.setPreferredSize(new Dimension(250, 35));
        cpfField.setFont(new Font("Arial", Font.PLAIN, 16));
        cpfField.setBorder(BorderFactory.createLineBorder(new Color(163, 196, 235), 2));

        initComponents();
    }

    private void initComponents() {
        // Painel de fundo com cor agradável
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(163, 196, 235);
                Color color2 = new Color(246, 250, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Painel central com cantos arredondados
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        loginPanel.setPreferredSize(new Dimension(350, 400));
        loginPanel.setOpaque(false);

        // Logo
        // Caminho relativo dentro do resources
        String logoPath = "/com/example/demo/views/reusable/full_logo_transparent.png";

        // Cria o ImageIcon original
        ImageIcon logoIcon = new ImageIcon(getClass().getResource(logoPath));

        // Redimensiona a imagem (por exemplo, largura 200px e altura 100px)
        Image img = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);

        // Cria o JLabel com a imagem
        JLabel logoLabel = new JLabel(logoIcon);

        // Se quiser manter texto também
        // JLabel logoLabel = new JLabel("Banco CVETTI", logoIcon, JLabel.CENTER);

        logoLabel.setFont(new Font("Arial Black", Font.BOLD, 28));
        logoLabel.setForeground(new Color(23, 59, 100));

        gbc.gridy = 0;
        loginPanel.add(logoLabel, gbc);

        // Boas-vindas
        JLabel welcomeLabel = new JLabel("Bem-vindo!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(23, 59, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        loginPanel.add(welcomeLabel, gbc);

        // CPF
        
        gbc.insets = new Insets(5, 0, 5, 0);
        cpfField = new JTextField();
        cpfField.setPreferredSize(new Dimension(250, 35));
        cpfField.setFont(new Font("Arial", Font.PLAIN, 16));
        cpfField.setBorder(BorderFactory.createLineBorder(new Color(163, 196, 235), 2));
        gbc.gridy = 2;
        loginPanel.add(cpfField, gbc);

        cpfErrorLabel = createErrorLabel();
        gbc.gridy = 3;
        loginPanel.add(cpfErrorLabel, gbc);

        // Senha
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(163, 196, 235), 2));
        gbc.gridy = 4;
        loginPanel.add(passwordField, gbc);

        passwordErrorLabel = createErrorLabel();
        gbc.gridy = 5;
        loginPanel.add(passwordErrorLabel, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Entrar");
        styleButton(loginButton, new Color(255, 222, 112), new Color(23, 59, 100));

        resetButton = new JButton("Limpar");
        styleButton(resetButton, new Color(23, 59, 100), Color.RED);

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 0, 5, 0);
        loginPanel.add(buttonPanel, gbc);

        // Links
        createAccountButton = new JButton("Criar conta");
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setForeground(new Color(23, 59, 100));
        createAccountButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 7;
        loginPanel.add(createAccountButton, gbc);

        exitButton = new JButton("Sair");
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setForeground(Color.RED);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 8;
        loginPanel.add(exitButton, gbc);

        // Adiciona listeners
        addListeners();

        // Adiciona loginPanel no centro do background
        backgroundPanel.add(loginPanel);
    }

    private void addListeners() {
        loginButton.addActionListener(e -> {
            try {
                handleLogin();
            } catch (SQLException ex) {
                showErrorDialog("Erro: " + ex.getMessage());
            }
        });

        resetButton.addActionListener(e -> resetFields());
        exitButton.addActionListener(e -> dispose());
        createAccountButton.addActionListener(e -> handleCreateAccount());

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        handleLogin();
                    } catch (SQLException ex) {
                        showErrorDialog("Erro: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel("");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void resetFields() {
        cpfField.setText("");
        passwordField.setText("");
        cpfErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    private void handleLogin() throws SQLException {
        String cpf = cpfField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        boolean valid = true;

        if (cpf.isEmpty()) {
            cpfErrorLabel.setText("CPF é obrigatório.");
            valid = false;
        } else if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            cpfErrorLabel.setText("Formato do CPF inválido (XXX.XXX.XXX-XX).");
            valid = false;
        } else {
            cpfErrorLabel.setText("");
        }

        if (password.isEmpty()) {
            passwordErrorLabel.setText("Senha é obrigatória.");
            valid = false;
        } else if (password.length() < 6) {
            passwordErrorLabel.setText("A senha deve ter pelo menos 6 caracteres.");
            valid = false;
        } else {
            passwordErrorLabel.setText("");
        }

        if (!valid) return;

        Connection conn = new UserConnection().conectar();
        UserDAOs userDAOs = new UserDAOs();
        User user = userDAOs.getUserByCpf(conn, cpf);

        if (user == null) {
            cpfErrorLabel.setText("CPF não cadastrado.");
            return;
        }

        String hashedPassword = PasswordHasher.hash(password);
        if (!user.getSenha().equals(hashedPassword)) {
            passwordErrorLabel.setText("Senha incorreta.");
            return;
        }

        Global.setLoggedInUser(cpf);
        JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + user.getNome());

        new MainPageController();
        dispose();
    }

    private void handleCreateAccount() {
        this.dispose(); // Fecha a tela de login
    
        JFrame createFrame = new JFrame("Criar Conta");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(500, 600);
        createFrame.setLocationRelativeTo(null);
    
        CreateAccountPageController createPanel = new CreateAccountPageController();
        createFrame.add(createPanel);
    
        createFrame.setVisible(true);
    }

    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}