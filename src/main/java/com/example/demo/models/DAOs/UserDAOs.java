package com.example.demo.models.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAOs {

    public void incluir(Connection conn) throws SQLException {
        String sql = "INSERT INTO USUARIO (nome, cpf, email, senha, balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, "John Doe");
            stm.setString(2, "12345678900");
            stm.setString(1, "knob@gnail.com");
            stm.setString(1, "12345678");
            stm.setFloat(1, 0.0f);
        }
    }
}
