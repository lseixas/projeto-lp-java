package com.example.demo.models.DAOs;

import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOs {
    public User createUser(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO `banco_cvetti_users`.`usuario` (`id`, `nome`, `email`, `cpf`, `senha`, `saldo`, `nascimento`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, user.getUserId().toString());
            stm.setString(2, user.getNome());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getCpf());
            stm.setString(5, user.getSenha());
            stm.setFloat(6, user.getSaldo());
            stm.setDate(7, user.getNascimento());
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                return user; // Retorna o usuário criado
            } else {
                return null;
            }
        }
    }

    public User getUserByCpf(Connection conn, String cpf) throws SQLException {
        String sql = "SELECT * FROM `banco_cvetti_users`.`usuario` WHERE `cpf` = ?;";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, cpf);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                float saldo = rs.getFloat("saldo");
                java.sql.Date nascimento = rs.getDate("nascimento");

                User user = new User(nome, email, cpf, senha, saldo, nascimento);

                return user;
            } else {
                return null;
            }
        }
    }

    public User getUserByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT * FROM `banco_cvetti_users`.`usuario` WHERE `email` = ?;";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String senha = rs.getString("senha");
                float saldo = rs.getFloat("saldo");
                java.sql.Date nascimento = rs.getDate("nascimento");

                User user = new User(nome, email, cpf, senha, saldo, nascimento);

                return user;
            } else {
                return null;
            }
        }
    }

    public User incrementUserSaldo(Connection conn, User user, double newSaldo) throws SQLException {
        String sql = "UPDATE `banco_cvetti_users`.`usuario` SET `saldo` = ? WHERE `cpf` = ?;";
        System.out.println("Updating user saldo for CPF: " + user.getCpf() + " to new saldo: " + newSaldo);
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setDouble(1, newSaldo + user.getSaldo());
            stm.setString(2, user.getCpf());
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                return new User(
                        user.getNome(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getSenha(),
                        (float) newSaldo + user.getSaldo(), // Atualiza o saldo para novo valor
                        user.getNascimento()
                );
            } else {
                return null;
            }
        }
    }
}
