package com.example.demo.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class Transaction {
    
    public static final String identifier = "Transaction";

    public enum Type {DEPOSITO, SAQUE, PIX}

    private String id;
    private Type type;
    private String pagaCpf;
    private String recebeCpf;
    private float valor;
    private LocalDateTime dadosDataHora;

    public Transaction(Type type, String pagaCpf, String recebeCpf, float valor){
        if(!validateAmount(valor)) throw new IllegalArgumentException("Valor invalido");
        
        if(!validadeAmount(pagaCpf)) throw new IllegalArgumentException("CPF de origem inválido");

        if(type == Type.PIX && !validateCpf(targetCpf))
            throw new IllegalArgumentException("CPF de destino inválido");

            this.id = UUID.randomUUID().toString();
            this.type = type;
            this.pagaCpf = pagaCpf;
            this.recebeCpf = recebeCpf;
            this.valor = valor;
            this.dadosDataHora = LocalDateTime();
    }

    public String getId(){ return id;}

    public Type getType(){ return type;}

    public String getPagaCpf(){ return pagaCpf;}

    public String getRecebeCpf(){ return recebeCpf;}

    public float getValor(){ return valor;}

    public LocalDateTime getDadosDataHora(){ return dadosDataHora;}

    public HashMap<String, Object> toDict() {
        return new HashMap<String, Object>() {{
            put("id", id);
            put("type", type.toString());
            put("pagaCpf", pagaCpf);
            put("recebeCpf", recebeCpf);
            put("valor", valor);
            put("DadosDataHora", dadosDataHora);
        }};
    }

    private boolean validateCpf(String cpf){
        //validate cpf logic
        return true;
    }

    private boolean validateValor(float valor){
        return valor > 0.0f;
    }

    public static Transaction novoDeposito(User user, float valor){
        return new Transaction(Type.DEPOSITO, user.getCpf(), user.getCpf(), valor);
    }

    public static Transaction novoSaque(User user, float valor){
        return new Transaction(Type.SAQUE, user.getCpf(), null, valor);
    }

    public static Transaction novoPix(User paga, User recebe, float valor) {
        return new Transaction(Type.PIX, paga.getCpf(), recebe.getCpf(), valor);
    }


}
