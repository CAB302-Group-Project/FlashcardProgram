package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.User;
import flashcard.Crypto.Hasher;

public class UserDAO {

    // Insert a new user
    public static void insertUser(String email, String passwordHash) {
        String sql = "Insert INTO users(email, password_hash) VALUES(?, ?)";

        try (Connection conn = db.DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            pstmt.executeUpdate();
            System.out.println("User Inserted: " + email);
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }

    // Fetch all users
    public static void getAllUsers() {
        String sql = "SELECT id, email, created_at FROM users";

        try (Connection conn = db.DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Users:");
            while (rs.next()) {
                System.out.println("ID " + rs.getInt("id")
                        + " | Email: " + rs.getString("email")
                        + " | Created: " + rs.getString("created_at"));
            }

        } catch (SQLException e) {
            System.err.println("Select failed: " + e.getMessage());
        }
    }

    public static User getUser(String email) {
        String sql = "SELECT id FROM users WHERE email=? LIMIT 1";
        try {
            Connection conn = db.DBConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                final int id = rs.getInt("id");
                return new User(id, email);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public static User authUser(String email, String password) {
        String sql = "SELECT id, password_hash FROM users WHERE email=? LIMIT 1";
        try {
            Connection conn = db.DBConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String hashed = rs.getString("password_hash");
                if (Hasher.verify(password, hashed)) {
                    return new User(id, email);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }
}
