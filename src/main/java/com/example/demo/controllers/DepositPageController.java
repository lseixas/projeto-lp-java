package com.example.demo.controllers;

import com.example.demo.util.PaymentEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DepositPageController {

    @FXML
    public ComboBox<String> paymentMethodCombo;

    public void Initialize() {
        // Initialize the payment method combo box with available payment methods

        for (PaymentEnum.PaymentType paymentType : PaymentEnum.PaymentType.values()) {
            paymentMethodCombo.getItems().add(paymentType.getDescription());
        }

        // Set a default selection if needed
        paymentMethodCombo.getSelectionModel().selectFirst();
    }

    public void handleCancel(ActionEvent actionEvent) {
        
    }

    public void handleConfirmDeposit(ActionEvent actionEvent) {
    }

    public void setQuickAmount(ActionEvent actionEvent) {

    }
}
