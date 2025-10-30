package com.example.demo.controllers;

import com.example.demo.models.entities.User;
import com.example.demo.util.Global;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AccountDisplayController extends JPanel {

    private JLabel nomeLabel;
    private JLabel cpfLabel;
    private JLabel emailLabel;
    private JLabel nascimentoLabel;
    private JLabel saldoLabel;

    private static User loggedUser;

    public AccountDisplayController() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ======= Cabeçalho =======
        JLabel titulo = new JLabel("👤 Meus Dados Pessoais", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;

        // ======= Campos =======
        gbc.gridy++;
        add(new JLabel("👨‍💼 Nome Completo:"), gbc);
        nomeLabel = new JLabel();
        gbc.gridx = 1;
        add(nomeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("📧 Email:"), gbc);
        emailLabel = new JLabel();
        gbc.gridx = 1;
        add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("📅 Data de Nascimento:"), gbc);
        nascimentoLabel = new JLabel();
        gbc.gridx = 1;
        add(nascimentoLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("🆔 CPF:"), gbc);
        cpfLabel = new JLabel();
        gbc.gridx = 1;
        add(cpfLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("💸 Saldo:"), gbc);
        saldoLabel = new JLabel();
        gbc.gridx = 1;
        add(saldoLabel, gbc);

        // ======= Botões =======
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel botoes = new JPanel();
        JButton editarBtn = new JButton("✏️ Editar Dados");
        JButton senhaBtn = new JButton("🔐 Alterar Senha");
        botoes.add(editarBtn);
        botoes.add(senhaBtn);

        add(botoes, gbc);

        // Eventos dos botões
        editarBtn.addActionListener(e -> editarDados());
        senhaBtn.addActionListener(e -> alterarSenha());

        // Inicializa os dados do usuário logado
        try {
            initialize();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do usuário: " + ex.getMessage());
        }
    }

    /** Método equivalente ao initialize() do JavaFX */
    public void initialize() throws SQLException {
        loggedUser = Global.getLoggedInUser();
        if (loggedUser != null) {
            nomeLabel.setText(loggedUser.getNome());
            cpfLabel.setText(loggedUser.getCpf());
            emailLabel.setText(loggedUser.getEmail());
            nascimentoLabel.setText(String.valueOf(loggedUser.getNascimento()));
            saldoLabel.setText("R$ " + loggedUser.getSaldo());
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum usuário logado encontrado.");
        }
    }

    // ==== Métodos auxiliares ====

    private void editarDados() {
        JOptionPane.showMessageDialog(this,
                "Função de edição ainda não implementada.",
                "Editar Dados",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void alterarSenha() {
        JPasswordField novaSenha = new JPasswordField();
        int resultado = JOptionPane.showConfirmDialog(this, novaSenha,
                "Digite a nova senha", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!");
        }
    }

    // ==== Main para teste ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Conta - Dados Pessoais");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new AccountDisplayController());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
