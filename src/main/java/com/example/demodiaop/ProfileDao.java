package com.example.demodiaop;

import java.sql.*;

public class ProfileDao {

    String getPasswordFromDb(String account) {
        String url = "jdbc:mysql://localhost:3306/test";
        String dbUsername = "sa";
        String dbPassword = "password";
        String sql = "SELECT password FROM account WHERE account = ?";
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, account);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("query database for password error, account: " + account, e);
        }
    }
}