package unit_test;

import db.DBConnector;
import db.DAO.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserCreationTest {

    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_PASSWORD = "hashedpassword";

    @BeforeEach
    public void cleanBefore() throws Exception {
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email = ?")) {
            stmt.setString(1, TEST_EMAIL);
            stmt.executeUpdate();
        }
    }

    @Test
    public void testInsertUser() {
        boolean result = UserDAO.insertUser(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
        assertTrue(result, "User should be inserted successfully");

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {

            stmt.setString(1, TEST_EMAIL);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "Inserted user should be found in the database");
            assertEquals(TEST_NAME, rs.getString("name"));
            assertEquals(TEST_PASSWORD, rs.getString("password_hash"));

        } catch (SQLException e) {
            fail("Verification query failed: " + e.getMessage());
        }
    }

    @Test
    public void testDuplicateEmailFails() {
        // First insert should succeed
        assertTrue(UserDAO.insertUser(TEST_EMAIL, TEST_NAME, TEST_PASSWORD));

        // Second insert should fail due to UNIQUE constraint
        boolean secondInsert = UserDAO.insertUser(TEST_EMAIL, "Another Name", "anotherhash");
        assertFalse(secondInsert, "Duplicate email insert should fail");
    }


    @AfterEach
    public void cleanAfter() throws Exception {
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email = ?")) {
            stmt.setString(1, TEST_EMAIL);
            stmt.executeUpdate();
        }
    }
}
