package com.example.demo.models.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserConnection {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "banco_cvetti_users";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static final String SERVER_CONNECTION_URL = "jdbc:mysql://" + HOST + ":" + PORT +
            "?user=" + USER + "&password=" + PASSWORD +
            "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String DB_CONNECTION_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
            "?user=" + USER + "&password=" + PASSWORD +
            "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String CREATE_DATABASE_SQL =
            "CREATE DATABASE IF NOT EXISTS " + DB_NAME +
                    " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

    private static final String CREATE_TABLE_USUARIO_SQL =
            "CREATE TABLE IF NOT EXISTS usuario (" +
                    "    id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "    nome VARCHAR(100) NOT NULL, " +
                    "    email VARCHAR(100) NOT NULL UNIQUE, " +
                    "    cpf VARCHAR(14) NOT NULL UNIQUE, " +
                    "    senha VARCHAR(255) NOT NULL, " +
                    "    saldo DECIMAL(15, 2) DEFAULT 0.00, " +
                    "    nascimento DATE" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Falha ao carregar o driver JDBC do MySQL.", e);
        }
    }

    public Connection conectar() throws SQLException {
        // Etapa 1: Garantir que o banco de dados exista
        try (Connection serverConnection = DriverManager.getConnection(SERVER_CONNECTION_URL);
             Statement stmt = serverConnection.createStatement()) {
            stmt.executeUpdate(CREATE_DATABASE_SQL);
        } catch (SQLException e) {
            System.err.println("Erro ao verificar/criar o banco de dados '" + DB_NAME + "': " + e.getMessage());
            throw new SQLException("Falha ao garantir a existência do banco de dados '" + DB_NAME + "'.", e);
        }

        // Etapa 2: Conectar-se ao banco de dados específico
        Connection dbConnection = DriverManager.getConnection(DB_CONNECTION_URL);

        // Etapa 3: Garantir que a tabela exista dentro desse banco de dados
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE_USUARIO_SQL);
        } catch (SQLException e) {
            try {
                dbConnection.close();
            } catch (SQLException closeEx) {
                System.err.println("Erro ao fechar a conexão após falha na criação da tabela: " + closeEx.getMessage());
                e.addSuppressed(closeEx);
            }
            System.err.println("Erro ao verificar/criar a tabela 'usuario' no banco '" + DB_NAME + "': " + e.getMessage());
            throw new SQLException("Falha ao garantir a existência da tabela 'usuario' no banco '" + DB_NAME + "'.", e);
        }

        return dbConnection;
    }
}