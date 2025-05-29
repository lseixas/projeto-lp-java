package com.example.demo.util;

public class PaymentEnum {
    public enum PaymentType {
        CREDIT_CARD("Crédito"),
        DEBIT_CARD("Débito"),
        PAYPAL("PayPal"),
        BANK_TRANSFER("TED"),
        PIX("PIX");

        private final String description;

        PaymentType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
