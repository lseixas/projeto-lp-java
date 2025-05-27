package com.example.demo.models.entities;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

public class User {

    public static String identifier = "User";

    private UUID userId;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private float saldo;
    private Date nascimento;

    public User(String nome,
                String email,
                String cpf,
                String senha,
                float saldo,
                Date nascimento) {

        this.userId = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.saldo = saldo;
        this.nascimento = nascimento;

    }

    public UUID getUserId() {
        return userId;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public float getSaldo() {
        return saldo;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void displayInfo() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + nome);
        System.out.println("Email: " + email);
        System.out.println("CPF: " + cpf);
        System.out.println("Password: " + senha);
        System.out.println("Balance: " + saldo);
        System.out.println("Birth Date: " + nascimento);
    }

    public HashMap<String, Object> toDict() {
        return new HashMap<String, Object>() {{
            put("userId", userId);
            put("nome", nome);
            put("email", email);
            put("cpf", cpf);
            put("senha", senha);
            put("saldo", saldo);
            put("nascimento", nascimento);
        }};
    }
    
    @Override
    public String toString(){
        return "User{" +
                "userId=" + userId +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", senha='" + senha + '\'' +
                ", saldo=" + saldo +
                ", nascimento=" + nascimento +
                '}';
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
