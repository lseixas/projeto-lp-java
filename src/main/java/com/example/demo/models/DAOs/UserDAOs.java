package com.example.demo.models.DAOs;

import com.example.demo.models.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public User getUserByCpf(Connection conn, String cpfToSearch) throws SQLException {
        /*
         * Retrieves a user from the database based on their CPF.
         *
         * @param conn The active database connection.
         * @param cpf  The CPF of the user to retrieve.
         * @return A User object if found, otherwise null.
         * @throws SQLException If a database access error occurs.
         */

        String sql = "SELECT id, nome, email, cpf, senha, balance FROM usuario WHERE cpf = ?";
        User user = null;

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, cpfToSearch); // Set the CPF parameter

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) { // Check if a user was found
                    // Retrieve data from ResultSet using the exact column names
                    int userIdFromDb = rs.getInt("id"); // 'id' column from your table
                    String nomeFromDb = rs.getString("nome");   // 'nome' column
                    String emailFromDb = rs.getString("email"); // 'email' column
                    String cpfFromDb = rs.getString("cpf");     // 'cpf' column
                    String senhaFromDb = rs.getString("senha"); // 'senha' column (password hash)
                    float balanceFromDb = rs.getFloat("balance"); // 'balance' column

                    // Create User object using its constructor:
                    // User(String name, String cpf, String email, String password)
                    // Note: Your User constructor performs validations. If data from the DB
                    // somehow violates these (e.g., legacy data or different rules),
                    // an IllegalArgumentException could be thrown here.
                    try {
                        user = new User(nomeFromDb, cpfFromDb, emailFromDb, senhaFromDb);

                        // The constructor initializes balance to 0.0f.
                        // Now, set the actual balance retrieved from the database using the setter.
                        user.setBalance(balanceFromDb); // This will also use your validateBalance() method.

                    } catch (IllegalArgumentException e) {
                        // This catch block is important if your User constructor's validations
                        // might fail with data already in the database.
                        System.err.println("Data integrity issue: Failed to create User object from database record for CPF "
                                + cpfToSearch + ". Validation error: " + e.getMessage());
                        // Depending on your error handling strategy, you might re-throw this as a custom exception
                        // or wrap it in an SQLException.
                        throw new SQLException("Error mapping database record to User object due to validation failure.", e);
                    }

                    // IMPORTANT NOTE on 'id':
                    // Your current 'User' class does not have a field to store the 'id_usuario'.
                    // We have fetched 'userIdFromDb', but it's not being set in the 'user' object.
                    // If you need to use the user's ID in your application logic after fetching it,
                    // you should modify your 'User' class to include an 'id' field,
                    // and a way to set it (e.g., a setter or a different constructor).
                    // For example:
                    // System.out.println("Fetched User ID: " + userIdFromDb + " (not stored in User object)");

                }
            } // ResultSet 'rs' is automatically closed here
        } // PreparedStatement 'stm' is automatically closed here

        return user; // Return the found user (with balance set) or null
    }
}
