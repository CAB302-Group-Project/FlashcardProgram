package db;

import org.sqlite.core.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Insert a new user
    public static void insertUser(String email, String passwordHash) {
        String sql = "Insert INTO users(email, password_hash) VALUES(?, ?)";

        try (Connection conn = db.DBConnector();
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
}
