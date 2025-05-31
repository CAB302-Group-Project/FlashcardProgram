package db.DAO;

import app.FlashcardApp;
import db.DBConnector;
import db.User;
import utilities.crypto.Hasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for managing user-related operations in the database.
 * Supports user registration, authentication, information retrieval, profile updates,
 * and account management.
 *
 * <p>This class underpins the authentication and user management features of the app,
 * such as login, signup, profile editing, and account initialization.</p>
 *
 *
 */
public class UserDAO
{
    /**
     * Inserts a new user and creates a default deck for them.
     *
     * @param name         the user's full name
     * @param email        the user's email
     * @param passwordHash the hashed password
     * @return true if insertion succeeded, false otherwise
     */
    public static boolean insertUser(String name, String email, String passwordHash) {
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

    /**
     * Displays all registered users in the database.
     * Intended for admin/debug use only.
     */
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

    /**
     * Retrieves a user's ID by their email address.
     *
     * @param email the user's email
     * @return the user's ID, or null if not found
     */
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

    /**
     * Retrieves a user object based on their user ID.
     *
     * @param userId the user's ID
     * @return the User object, or null if not found
     */
    public static db.User getUserById(int userId) {
        String sql = "SELECT name, email FROM users WHERE id = ?";
        try {
            Connection conn = FlashcardApp.getInstance().getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                return new User(userId, name, email);
            }
        } catch (SQLException e) {
            System.err.println("Get user ID failed: " + e.getMessage());
        }

        return null;
    }

    /**
     * Checks whether a user exists by email.
     *
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    public static boolean userExists(String email) {
        return getUserIdByEmail(email) != null;
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the user's email
     * @return the User object, or null if not found
     */
    public static User getUser(String email) {
        String sql = "SELECT id, name FROM users WHERE email=? LIMIT 1";
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

    /**
     * Logs a successful login for a user.
     *
     * @param conn   the database connection
     * @param userId the user's ID
     */
    private static void logLogin(Connection conn, int userId) {
        String sql = "INSERT INTO login_log (user_id) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to log login: " + e.getMessage());
        }
    }

    /**
     * Registers a user by calling insertUser.
     *
     * @param name     the name of the user
     * @param email    the email of the user
     * @param password the hashed password
     * @return true if registration succeeded
     */
    public static boolean registerUser(String name, String email, String password) {
        return insertUser(name, email, password);
    }

    /**
     * Authenticates a user with email and plaintext password.
     *
     * @param email    the user's email
     * @param password the password to compare (plaintext for now)
     * @return the authenticated User object, or null if invalid
     */
    public static User authUser(String email, String password) {
        String sql = "SELECT id, name, email, password_hash FROM users WHERE email=? LIMIT 1";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String storedPassword = rs.getString("password_hash");
                String name = rs.getString("name");
                String emailResult = rs.getString("email");

                if (Hasher.verify(password, storedPassword)) {
                    logLogin(conn, id);
                    return new User(id, name, email);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during auth: " + e.getMessage());
        }

        return null;
    }

    /**
     * Inserts a test user into the database.
     * Used for testing purposes.
     */
    public static void insertTestUser() {
        String email = "test@example.com";
        String name = "Test Name";
        String password = "test123";

        String deleteSQL = "DELETE FROM users WHERE email = ?";
        String insertSQL = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.connect()) {
            try (PreparedStatement del = conn.prepareStatement(deleteSQL)) {
                del.setString(1, email);
                del.executeUpdate();
            }

            try (PreparedStatement ins = conn.prepareStatement(insertSQL)) {
                ins.setString(1, name);
                ins.setString(2, email);
                ins.setString(3, password);
                ins.executeUpdate();
            }

            System.out.println("Test user recreated: " + email + " / " + password);
            System.out.println("DB PATH = " + DBConnector.connect().getMetaData().getURL());

        } catch (SQLException e) {
            System.err.println("Failed to recreate test user: " + e.getMessage());
        }
    }

    /**
     * Updates a user's password with a new hashed value.
     *
     * @param userId           the user's ID
     * @param newHashedPassword the new hashed password
     * @return true if update was successful
     */
    public static boolean updateUserPassword(int userId, String newHashedPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHashedPassword);
            stmt.setInt(2, userId);
            int updated = stmt.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a user's name.
     *
     * @param userId  the user's ID
     * @param newName the new name
     */
    public static void updateName(int userId, String newName) {
        String sql = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a user's email.
     *
     * @param userId   the user's ID
     * @param newEmail the new email
     */
    public static void updateEmail(int userId, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether an email already exists in the database.
     *
     * @param email the email to check
     * @return true if the email exists; false if not or if error occurs
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

}
