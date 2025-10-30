package com.example.demo;

import com.example.demo.controllers.LoginPageController;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        // Garante aparência consistente do Swing (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Inicia a janela de login
        SwingUtilities.invokeLater(() -> {
            LoginPageController loginPage = new LoginPageController();
            loginPage.setVisible(true);
        });
    }
}