package com.example.demo.models.DAOs;

import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAOs {

    public void createUser(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO `banco_cvetti_users`.`usuario` (`id`, `nome`, `email`, `cpf`, `senha`, `saldo`, `nascimento`) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, user.getUserId().toString()); // Set the user ID
            stm.setString(2, user.getNome());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getCpf());
            stm.setString(5, user.getSenha());
            stm.setFloat(6, user.getSaldo());
            stm.setDate(7, user.getNascimento()); // Assuming getBirthDate() returns java.sql.Date
            stm.executeUpdate();
        }
    }
}
