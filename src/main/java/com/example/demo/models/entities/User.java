package com.example.demo.models.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class User {

    public static String identifier = "User";

    private String name;
    private String cpf;
    private String email;
    private String password;
    private float balance;

    private final List<String> historico = new ArrayList<>();

    public User(String name, String cpf, String email, String password) {

        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (!validadeCpf(cpf)) {
            throw new IllegalArgumentException("Invalid CPF");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!validatePassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!validateBalance(balance)) {
            throw new IllegalArgumentException("Invalid balance");
        }

        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.balance = 0.0f;

    }

    /* Operacoes Financeiras */

    public Transaction depositar(float valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor deve ser positivo");
        balance += valor;
        Transaction tx = Transaction.novoDeposito(this, valor);
        historico.add(tx.getId());
        return tx;
    }

    public Transaction sacar(float valor) {
        if (valor <= 0) throw new IllegalArgumentException("Valor deve ser positivo");
        if (balance < valor) throw new IllegalArgumentException("Saldo insuficiente");
        balance -= valor;
        Transaction tx = Transaction.novoSaque(this, valor);
        historico.add(tx.getId());
        return tx;
    }

    public Transaction pix(User destino, float valor) {
        if (destino == null) throw new IllegalArgumentException("Destinatário nulo");
        if (valor <= 0) throw new IllegalArgumentException("Valor deve ser positivo");
        if (balance < valor) throw new IllegalArgumentException("Saldo insuficiente");

        balance          -= valor;
        destino.balance  += valor;

        Transaction tx = Transaction.novoPix(this, destino, valor);
        historico.add(tx);
        destino.historico.add(tx);
        return tx;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.name = name;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        if (!validadeCpf(cpf)) {
            throw new IllegalArgumentException("Invalid CPF");
        }
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }
    public String getPassword() {
        if (!validatePassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return password;
    }
    public void setPassword(String password) {
        if (!validatePassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        this.password = password;
    }
    public float getBalance() {
        if (!validateBalance(balance)) {
            throw new IllegalArgumentException("Invalid balance");
        }
        return balance;
    }
    public void setBalance(float balance) {
        if (!validateBalance(balance)) {
            throw new IllegalArgumentException("Invalid balance");
        }
        this.balance = balance;
    }

    public List<Transaction> getHistorico() {
        return historico;
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

    public boolean validateName(String name){
        //validate name logic
        return true;
    }

    public boolean validadeCpf(String cpf){
        //validate cpf logic
        return true;
    }

    public boolean validateEmail(String email){
        //validate email logic
        return true;
    }

    public boolean validatePassword(String password){
        //validate password logic
        return true;
    }

    public boolean validateBalance(float balance){
        //validate balance logic
        return true;
    }

}
