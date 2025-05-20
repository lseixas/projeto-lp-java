package com.example.demo.models.DAOs;

import com.example.demo.models.entities.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOs {

    public void criarTransaction(Connection conn, Transaction tx) throws SQLException {
        String sql = "INSERT INTO transactions (id, type, paga_cpf, recebe_cpf, valor, data_hora) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, tx.getId());
            stm.setString(2, tx.getType().name());
            stm.setString(3, tx.getPagaCpf());
            stm.setString(4, tx.getRecebeCpf());          
            stm.setFloat (5, tx.getValor());
            stm.setTimestamp(6, Timestamp.valueOf(tx.getDataHora()));
            stm.executeUpdate();
        }
    }

    public List<Transaction> listar(Connection conn) throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY data_hora DESC";
        try (PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            return mapResultSet(rs);
        }
    }

    public List<Transaction> listarPorCpf(Connection conn, String cpf) throws SQLException {
        String sql = "SELECT * FROM transactions " +
                     "WHERE paga_cpf = ? OR recebe_cpf = ? " +
                     "ORDER BY data_hora DESC";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, cpf);
            stm.setString(2, cpf);
            try (ResultSet rs = stm.executeQuery()) {
                return mapResultSet(rs);
            }
        }
    }

    private List<Transaction> mapResultSet(ResultSet rs) throws SQLException {
        List<Transaction> lista = new ArrayList<>();
        while (rs.next()) {
            Transaction.Type type = Transaction.Type.valueOf(rs.getString("type"));
            String id        = rs.getString("id");              // para preencher depois via reflection
            String pagaCpf   = rs.getString("paga_cpf");
            String recebeCpf = rs.getString("recebe_cpf");
            float  valor     = rs.getFloat("valor");
            LocalDateTime dh = rs.getTimestamp("data_hora").toLocalDateTime();

            Transaction tx;
            switch (type) {
                case DEPOSITO -> tx = Transaction.novoDepositoFake(id, pagaCpf, valor, dh);
                case SAQUE    -> tx = Transaction.novoSaqueFake   (id, pagaCpf, valor, dh);
                case PIX      -> tx = Transaction.novoPixFake     (id, pagaCpf, recebeCpf, valor, dh);
                default -> throw new SQLException("Tipo desconhecido: " + type);
            }
            lista.add(tx);
        }
        return lista;
    }
}
