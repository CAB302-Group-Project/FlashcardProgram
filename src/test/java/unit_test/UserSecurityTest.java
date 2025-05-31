package unit_test;

import db.DBConnector;
import db.DAO.UserDAO;
import db.User;
import db.utils.DBInit;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserSecurityTest {

    private static final String TEST_DB = "test_users.db";
    private static final String PASSWORD = "test123";
    private static final String PASSWORD_HASHED = "$2a$10$lH0JZWYjqrwP9/GOb06THey9SCa/a5FElyuv9CRl4M4.4l/f3/5oe";
    private String testEmail;
    private String testName;

    @BeforeAll
    public void setupTestDB() {
        System.setProperty("db.name", TEST_DB);
        DBInit.main(null); // Create schema
    }

    @BeforeEach
    public void generateUniqueUser() throws SQLException {
        testEmail = "user_" + UUID.randomUUID() + "@example.com";
        testName = "Name_" + UUID.randomUUID();
        try (Connection conn = DBConnector.connect()) {
            conn.createStatement().executeUpdate("DELETE FROM login_log");
            conn.createStatement().executeUpdate("DELETE FROM users");
        }
    }

    @Test
    public void testRegisterAndAuthenticateUser() {
        assertTrue(UserDAO.registerUser(testName, testEmail, PASSWORD_HASHED), "Registration should succeed");

        User user = UserDAO.authUser(testEmail, PASSWORD);
        assertNotNull(user);
        assertEquals(testName, user.getName());
        assertEquals(testEmail, user.getEmail());
    }

    @Test
    public void testDuplicateEmailFails() {
        assertTrue(UserDAO.registerUser(testName, testEmail, PASSWORD));
        boolean secondAttempt = UserDAO.registerUser("Other Name", testEmail, "otherpass");
        assertFalse(secondAttempt, "Should fail due to duplicate email");
    }

    @Test
    public void testWrongPasswordFails() {
        UserDAO.registerUser(testName, testEmail, PASSWORD);
        assertNull(UserDAO.authUser(testEmail, "wrongpass"));
    }

    @Test
    public void testNonexistentEmailFails() {
        assertNull(UserDAO.authUser("ghost@example.com", "anything"));
    }

    @Test
    public void testLoginLogWritten() throws SQLException {
        UserDAO.registerUser(testName, testEmail, PASSWORD_HASHED);
        UserDAO.authUser(testEmail, PASSWORD);

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("""
                     SELECT COUNT(*) AS count FROM login_log 
                     WHERE user_id = (SELECT id FROM users WHERE email = ?)""")) {
            stmt.setString(1, testEmail);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertTrue(rs.getInt("count") > 0, "Login should be logged");
        }
    }

    @AfterAll
    public void deleteTestDB() {
        new File(TEST_DB).delete();
    }
}
