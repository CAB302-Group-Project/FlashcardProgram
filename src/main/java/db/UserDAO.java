package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO
{
    // Insert a new user
    public static boolean insertUser(String email, String passwordHash) {
        String userInsertSQL = "INSERT INTO users(email, password_hash) VALUES(?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(userInsertSQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, email);
            stmt.setString(2, passwordHash);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) return false;

            // Get generated user ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Create a default deck for the user
                    DeckDAO.insertDeck(userId, "Default Deck", "Automatically created for new user");

                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Insert user failed: " + e.getMessage());
        }

        return false;
    }

    // Fetch all users
    public static void getAllUsers()
    {
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

    public static Integer getUserIdByEmail(String email)
    {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("Get user ID failed: " + e.getMessage());
        }
        return null;
    }

    public static boolean userExists(String email) {
        return getUserIdByEmail(email) != null;
    }

}
