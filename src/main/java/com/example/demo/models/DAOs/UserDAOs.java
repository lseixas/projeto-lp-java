package com.example.demo.models.DAOs;

import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAOs {

    public void createUser(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha, balance) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, user.getName());
            stm.setString(2, user.getCpf());
            stm.setString(3, user.getEmail());
            stm.setString(4, user.getPassword());
            stm.setFloat(5, user.getBalance());
            stm.executeUpdate();
        }
    }
}
