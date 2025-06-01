package unit_test;

import db.DAO.DeckDAO;
import db.DAO.FlashcardDAO;
import db.DBConnector;
import db.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckFlashcardFlowTest {
    private static int testUserId;

    @BeforeAll
    public static void setUpUser() throws Exception {
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users(email, name, password_hash) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "testuser@example.com");
            stmt.setString(2, "Test User");
            stmt.setString(3, "hashedpassword");
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    testUserId = keys.getInt(1);
                }
            }
        }
    }

    @Test
    public void testDeckAndFlashcardInsertion() {
        String title = "Test Deck";
        String description = "Test Description";

        int deckId = DeckDAO.insertDeck(testUserId, title, description);
        assertTrue(deckId > 0, "Deck should be inserted and return valid ID");

        boolean flashcardInserted = FlashcardDAO.insertFlashcard(deckId, "What is Java?", "A programming language.");
        assertTrue(flashcardInserted, "Flashcard should be inserted successfully");

        // Verify flashcard is linked to the deck
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flashcards WHERE deck_id = ?")) {
            stmt.setInt(1, deckId);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "At least one flashcard should be linked to the deck");
                assertEquals("What is Java?", rs.getString("front"));
                assertEquals("A programming language.", rs.getString("back"));
            }
        } catch (Exception e) {
            fail("Database verification failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        try (Connection conn = DBConnector.connect();
             PreparedStatement cleanup = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            cleanup.setInt(1, testUserId);
            cleanup.executeUpdate();
        }
    }
}
