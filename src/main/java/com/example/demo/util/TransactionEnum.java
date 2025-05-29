package com.example.demo.util;

public enum TransactionEnum {
    DEPOSIT("Deposit Transaction"),
    WITHDRAW("Withdraw Transaction"),
    TRANSFER("Transfer Transaction");

    private final String description;

    TransactionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
