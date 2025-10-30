package com.example.demo.controllers;

import com.example.demo.models.entities.User;
import com.example.demo.util.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class MainPageController extends JFrame {

    private static User loggedUser;

    private JButton accountButton;
    private JButton depositButton;

    public MainPageController() {
        setTitle("Banco CVETTI - Main Page");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        try {
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // Botão Conta
        accountButton = new JButton("Minha Conta");
        accountButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleAccountButtonClick();
            }
        });

        // Botão Depósito
        depositButton = new JButton("Depósito");
        depositButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleDepositButtonClick();
            }
        });

        add(new JLabel("Bem-vindo, " + (loggedUser != null ? loggedUser.getNome() : "Usuário")));
        add(accountButton);
        add(depositButton);

        setVisible(true);
    }

    public void initialize() throws SQLException {
        loggedUser = Global.getLoggedInUser();
    }

    private void handleAccountButtonClick() {
        // Fecha a tela atual e abre a tela de exibição de conta
        dispose();
        new AccountDisplayController();
    }

    private void handleDepositButtonClick() {
        // Fecha a tela atual e abre a tela de depósito
        dispose();
        new DepositPageController();
    }
}
