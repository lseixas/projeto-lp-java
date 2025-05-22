package com.example.demo.models.connection;

import com.example.demo.models.DAOs.UserDAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserConnection {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection conectar() throws SQLException {
        String servidor = "localhost";
        String porta = "3306";
        String banco = "banco_cvetti_users";
        String usuario = "root";
        String senha = "root";
        return DriverManager.getConnection(
                "jdbc:mysql://" + servidor + ":" + porta + "/" + banco + "?user=" + usuario + "&password=" + senha);
    }
}
