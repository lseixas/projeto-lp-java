package com.example.demo.models.DAOs;

import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAOs {

    public User createUser(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO `banco_cvetti_users`.`usuario` (`id`, `nome`, `email`, `cpf`, `senha`, `saldo`, `nascimento`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, user.getUserId().toString()); // Set the user ID
            stm.setString(2, user.getNome());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getCpf());
            stm.setString(5, user.getSenha());
            stm.setFloat(6, user.getSaldo());
            stm.setDate(7, user.getNascimento()); // Assuming getBirthDate() returns java.sql.Date
            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                return user; // Return the created user
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
                UUID userId = UUID.fromString(rs.getString("id"));
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
                UUID userId = UUID.fromString(rs.getString("id"));
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
}
