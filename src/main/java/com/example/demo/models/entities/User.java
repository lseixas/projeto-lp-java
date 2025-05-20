package com.example.demo.models.entities;

import java.util.HashMap;

public class User {

    static String identifier = "User";

    private String name;

    private String cpf;
    private String email;
    private String password;

    private float balance;

    public User(String name, String cpf, String email, String password) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.balance = 0.0f;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("CPF: " + cpf);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Balance: " + balance);
    }

    public HashMap<String, Object> toDict() {
        return new HashMap<String, Object>() {{
            put("name", name);
            put("cpf", cpf);
            put("email", email);
            put("password", password);
            put("balance", balance);
        }};
    }

}
