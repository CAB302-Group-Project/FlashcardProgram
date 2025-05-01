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

<<<<<<< Updated upstream
=======
    public static User getUser(String email) {
        String sql = "SELECT id FROM users WHERE email=? LIMIT 1";
        try {
            Connection conn = DBConnector.connect();
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

    private static void logLogin(int userId) {
        String sql = "INSERT INTO login_log (user_id) VALUES (?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to log login: " + e.getMessage());
        }
    }

    public static boolean registerUser(String email, String password) {
        String sql = "INSERT INTO users (email, password_hash) VALUES (?, ?)";
        String hashed = Hasher.hash(password);  // Assuming Hasher.hash() exists

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, hashed);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static User authUser(String email, String password) {
        String sql = "SELECT id, password_hash FROM users WHERE email=? LIMIT 1";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String hashed = rs.getString("password_hash");

                if (Hasher.verify(password, hashed)) {
                    logLogin(id);  // Log successful login here
                    return new User(id, email);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during auth: " + e.getMessage());
        }

        return null;  // Only reached if login fails
    }

    public static void insertTestUser() {
        String email = "test@example.com";
        String password = "test123";
        String hashed = Hasher.hash(password);  // BCrypt hash

        // First, delete the test user if it exists
        String deleteSQL = "DELETE FROM users WHERE email = ?";

        // Then insert fresh
        String insertSQL = "INSERT INTO users (email, password_hash) VALUES (?, ?)";

        try (Connection conn = DBConnector.connect()) {
            // Delete if exists
            try (PreparedStatement del = conn.prepareStatement(deleteSQL)) {
                del.setString(1, email);
                del.executeUpdate();
            }

            // Insert new user
            try (PreparedStatement ins = conn.prepareStatement(insertSQL)) {
                ins.setString(1, email);
                ins.setString(2, hashed);
                ins.executeUpdate();
            }

            System.out.println("Test user recreated: " + email + " / " + password);

        } catch (SQLException e) {
            System.err.println("Failed to recreate test user: " + e.getMessage());
        }
    }


>>>>>>> Stashed changes
}
