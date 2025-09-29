package com.example.servlet;

import java.sql.*;

public class UserDao {

    public static void save(User user) throws SQLException {
        String sql = "INSERT INTO users(login, password, email) VALUES(?, ?, ?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());

            ps.executeUpdate();
        }
    }

    public static User find(String login) throws SQLException {
        String sql = "SELECT login, password, email FROM users WHERE login = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) { // для SELECT
                if (rs.next()) { // есть строка
                    return new User(
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
                return null; // ничего не нашли
            }
        }
    }

    public static boolean checkCredentials(String login, String password) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ? AND password = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}