package db.DAO;

import app.FlashcardApp;
import db.DBConnector;
import db.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO
{
    // Insert a new user
    public static boolean insertUser(String email, String name, String passwordHash) {
        String userInsertSQL = "INSERT INTO users(name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(userInsertSQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
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

    public static db.User getUserById(int userId) {
        String sql = "SELECT email FROM users WHERE id = ?";
        try {
            Connection conn = FlashcardApp.getInstance().getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                return new User(userId, name, email);
            }
        } catch (SQLException e) {
            System.err.println("Get user ID failed: " + e.getMessage());
        }

        return null;
    }

    public static boolean userExists(String email) {
        return getUserIdByEmail(email) != null;
    }

    public static User getUser(String email) {
        String sql = "SELECT id FROM users WHERE email=? LIMIT 1";
        try {
            Connection conn = DBConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                return new User(id, name, email);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private static void logLogin(Connection conn, int userId) {
        String sql = "INSERT INTO login_log (user_id) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to log login: " + e.getMessage());
        }
    }

    public static boolean registerUser(String email, String name, String password) {
        return insertUser(email, name, password);
    }

    public static User authUser(String email, String password) {
        String sql = "SELECT id, name, password_hash FROM users WHERE email=? LIMIT 1";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String storedPassword = rs.getString("password_hash");
                String name = rs.getString("name");

                if (password.equals(storedPassword)) {
                    logLogin(conn, id);
                    return new User(id, name, email);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during auth: " + e.getMessage());
        }

        return null;
    }

    public static void insertTestUser() {
        String email = "test@example.com";
        String name = "Test Name";
        String password = "test123";

        String deleteSQL = "DELETE FROM users WHERE email = ?";
        String insertSQL = "INSERT INTO users (email, name, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.connect()) {
            try (PreparedStatement del = conn.prepareStatement(deleteSQL)) {
                del.setString(1, email);
                del.executeUpdate();
            }

            try (PreparedStatement ins = conn.prepareStatement(insertSQL)) {
                ins.setString(1, email);
                ins.setString(2, name);
                ins.setString(3, password);
                ins.executeUpdate();
            }

            System.out.println("Test user recreated: " + email + " / " + password);

        } catch (SQLException e) {
            System.err.println("Failed to recreate test user: " + e.getMessage());
        }
    }
}
