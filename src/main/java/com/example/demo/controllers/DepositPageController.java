package com.example.demo.controllers;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;
import com.example.demo.util.Global;
import com.example.demo.util.PaymentEnum;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DepositPageController {

    @FXML
    public ComboBox<String> paymentMethodCombo;
    public Button quick50;
    public Button quick100;
    public Button quick200;
    public Button quick500;
    public Label currentBalanceLabel;

    static User loggedUser;
    public TextField amountField;
    public Label depositAmountFieldError;
    public Label depositPaymentMethodError;

    public void initialize() throws SQLException {
        // Initialize the payment method combo box with available payment methods

        loggedUser = Global.getLoggedInUser();

        currentBalanceLabel.setText(
                NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(loggedUser.getSaldo())
        );

        amountField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                String digitsOnly = newValue.replaceAll("[^\\d]", "");

                if (!digitsOnly.equals(newValue)) {
                    amountField.setText(digitsOnly);
                }

            }
        });

        for (PaymentEnum.PaymentType paymentType : PaymentEnum.PaymentType.values()) {
            paymentMethodCombo.getItems().add(paymentType.getDescription());
        }

        // Set a default selection if needed
        paymentMethodCombo.getSelectionModel().selectFirst();

    }

    public void handleCancel(ActionEvent actionEvent) {
        
    }

    public void handleConfirmDeposit(MouseEvent mouseEvent) throws SQLException {

        boolean allFieldsValid = true;

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {

            allFieldsValid &= validateSaldo(amountField, depositAmountFieldError);
            allFieldsValid &= validatePaymentMethod(paymentMethodCombo.getValue(), depositPaymentMethodError);

            if (allFieldsValid) {

                String amountText = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(
                        Double.parseDouble(amountField.getText())
                );

                System.out.println("Prosseguindo com o depósito de: " + amountText + " via " + paymentMethodCombo.getValue());

                Connection userDbConnection = new UserConnection().conectar();
                UserDAOs userDAOs = new UserDAOs();

                User updated_user = userDAOs.incrementUserSaldo(
                        userDbConnection,
                        loggedUser,
                        Double.parseDouble(amountField.getText())
                );

                if(updated_user != null) {
                    updated_user.displayInfo();
                    loggedUser = updated_user;
                    currentBalanceLabel.setText(
                            NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(loggedUser.getSaldo())
                    );
                }
                else {
                    System.out.println("Erro ao atualizar o saldo do usuário.");
                }


            }

        }

    }

    public boolean validateSaldo(TextField field, Label errorLabel) {

        String saldo = field.getText().trim();
        if (saldo == null || saldo.isEmpty()) {
            showError(errorLabel, "Valor do depósito é obrigatório.");
            return false;
        }
        if (Double.parseDouble(saldo) <= 0) {
            showError(errorLabel, "Valor deve ser maior que zero.");
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    public boolean validatePaymentMethod(String paymentMethod, Label errorLabel) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            showError(errorLabel, "Método de pagamento é obrigatório.");
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
            errorLabel.setText(""); // Clear the text
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
    }

    public void setQuickAmount(MouseEvent mouseEvent) throws ParseException {

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY) {
            String balanceText = amountField.getText();

            if (balanceText == null || balanceText.isEmpty()) {
                balanceText = "0.00"; // Default to 0 if the field is empty
            }

            double addedAmount = 0;
            double currentMoney = Double.parseDouble(balanceText);

            if (mouseEvent.getSource() == quick50) {
                addedAmount = 50.0;
            } else if (mouseEvent.getSource() == quick100) {
                addedAmount = 100.0;
            } else if (mouseEvent.getSource() == quick200) {
                addedAmount = 200.0;
            } else if (mouseEvent.getSource() == quick500) {
                addedAmount = 500.0;
            }

            double updatedBalance = currentMoney + addedAmount;

            System.out.println("Updated Balance: " + updatedBalance);

            DecimalFormat df = new DecimalFormat("#");
            amountField.setText(df.format(updatedBalance));
        }
    }
}
