package com.example.demo.util;

import com.example.demo.models.DAOs.UserDAOs;
import com.example.demo.models.connection.UserConnection;
import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.SQLException;

public class Global {
    public static User loggedInUser = null;
    public static String loggedInCpf = null;

    public Global(){
    }

    public static void setLoggedInUser(String cpf) throws SQLException {
        Connection conn = new UserConnection().conectar();
        UserDAOs userDAOs = new UserDAOs();

        User user = userDAOs.getUserByCpf(conn, cpf);

        loggedInCpf = cpf;
        loggedInUser = user;
    }

    public static void refreshLoggedInUser() throws SQLException {
        Connection conn = new UserConnection().conectar();
        UserDAOs userDAOs = new UserDAOs();

        loggedInUser = userDAOs.getUserByCpf(conn, loggedInCpf);
    }

    public static User getLoggedInUser() throws SQLException {
        refreshLoggedInUser();
        return loggedInUser;
    }
}
