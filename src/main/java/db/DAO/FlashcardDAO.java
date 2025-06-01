package db.DAO;

import db.DBConnector;
import db.Flashcard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing flashcards in the database.
 * Provides methods to insert, retrieve, update, and delete flashcards,
 * as well as manage spaced repetition metadata.
 *
 * <p>Flashcards are linked to decks via a foreign key relationship.</p>
 *
 * @author YourName
 */

public class FlashcardDAO
{

    /**
     * Inserts a flashcard into a specified deck with full details including optional image.
     *
     * @param deckId     the ID of the deck
     * @param front      the front content of the flashcard
     * @param back       the back content of the flashcard
     * @param mediaType  the media type (e.g., "text", "image")
     * @param difficulty the difficulty level (e.g., "easy", "medium", "hard")
     * @param imagePath  the image path (nullable)
     * @return true if the insert was successful, false otherwise
     */
    public static boolean insertFlashcard(int deckId, String front, String back, String mediaType, String difficulty, String imagePath)
    {
        String sql = "INSERT INTO flashcards (\n" +
                "    deck_id, front, back, media_type, difficulty, image_path, \n" +
                "    repetitions, easiness_factor, last_reviewed_at, next_review_at\n" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL, date('now'))";

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            pstmt.setString(2, front);
            pstmt.setString(3, back);
            pstmt.setString(4, mediaType);
            pstmt.setString(5, difficulty);
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, 0);                      // repetitions
            pstmt.setDouble(8, 2.5);                 // ease factor
            pstmt.executeUpdate();
            return true;

        }
        catch (SQLException e)
        {
            System.err.println("Insert flashcard failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all flashcards for a specific deck.
     *
     * @param deckId the ID of the deck
     * @return a list of Flashcard objects
     */
    public static List<Flashcard> getFlashcardsByDeckId(int deckId)
    {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE deck_id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, deckId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flashcard card = new Flashcard(
                        rs.getInt("id"),
                        rs.getInt("deck_id"),
                        rs.getString("front"),
                        rs.getString("back"),
                        rs.getString("media_type"),
                        rs.getString("difficulty"),
                        rs.getString("created_at"),
                        rs.getString("image_path"),
                        rs.getInt("repetitions"),
                        rs.getDouble("easiness_factor"),
                        rs.getString("last_reviewed_at"),
                        rs.getString("next_review_at")
                );
                flashcards.add(card);
            }

        }
        catch (SQLException e) {
            System.err.println("Fetch flashcards failed: " + e.getMessage());
        }

        return flashcards;
    }

    /**
     * Retrieves a single flashcard by its ID.
     *
     * @param id the ID of the flashcard
     * @return the Flashcard object if found, or null if not found
     */
    public static Flashcard getFlashcardById(int id)
    {
        String sql = "SELECT * FROM flashcards WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return new Flashcard(
                        rs.getInt("id"),
                        rs.getInt("deck_id"),
                        rs.getString("front"),
                        rs.getString("back"),
                        rs.getString("media_type"),
                        rs.getString("difficulty"),
                        rs.getString("created_at"),
                        rs.getString("image_path"),
                        rs.getInt("repetitions"),
                        rs.getDouble("easiness_factor"),
                        rs.getString("last_reviewed_at"),
                        rs.getString("next_review_at")
                );
            }

        }
        catch (SQLException e) {
            System.err.println("Get flashcard by ID failed: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the content and metadata of a flashcard.
     *
     * @param id         the ID of the flashcard
     * @param front      the new front content
     * @param back       the new back content
     * @param difficulty the new difficulty level
     * @param mediaType  the new media type
     * @param imagePath  the new image path (nullable)
     * @return true if updated successfully, false otherwise
     */
    public static boolean updateFlashcard(int id, String front, String back, String difficulty, String mediaType, String imagePath)
    {
        String sql = "UPDATE flashcards SET front = ?, back = ?, difficulty = ?, media_type = ?, image_path = ? WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, front);
            stmt.setString(2, back);
            stmt.setString(3, difficulty);
            stmt.setString(4, mediaType);
            stmt.setString(5, imagePath);
            stmt.setInt(6, id);
            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e)
        {
            System.err.println("Update flashcard failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a flashcard from the database.
     *
     * @param cardId the ID of the flashcard to delete
     * @return true if deleted successfully, false otherwise
     */
    public static boolean deleteFlashcard(int cardId) {
        String sql = "DELETE FROM flashcards WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, cardId);
            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e)
        {
            System.err.println("Delete flashcard failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts a flashcard with only front and back text (simplified overload).
     *
     * @param deckId the ID of the deck
     * @param front  the front content
     * @param back   the back content
     * @return true if inserted successfully, false otherwise
     */
    public static boolean insertFlashcard(int deckId, String front, String back) {
        return insertFlashcard(deckId, front, back, "text", "medium", null);
    }

    /**
     * Updates the difficulty of a flashcard.
     *
     * @param flashcardId the ID of the flashcard
     * @param difficulty  the new difficulty level
     * @throws SQLException if the update fails
     */
    public static void updateFlashcardDifficulty(int flashcardId, String difficulty) throws SQLException {
        String sql = "UPDATE flashcards SET difficulty = ? WHERE id = ?";

        try (Connection conn = DBConnector.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, difficulty);
            stmt.setInt(2, flashcardId);
            stmt.executeUpdate();
        }
    }


    /**
     * Updates spaced repetition parameters for a flashcard.
     *
     * @param flashcardId     the ID of the flashcard
     * @param repetitions     the new repetition count
     * @param easinessFactor  the updated easiness factor
     * @param lastReviewedAt  the timestamp of last review
     * @param nextReviewAt    the next scheduled review timestamp
     * @throws SQLException if the update fails
     */
    public static void updateSpacedRepetitionData(int flashcardId, int repetitions,
                                                  double easinessFactor, String lastReviewedAt, String nextReviewAt) throws SQLException {
        String sql = "UPDATE flashcards SET repetitions = ?, easiness_factor = ?, " +
                "last_reviewed_at = ?, next_review_at = ? WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, repetitions);
            stmt.setDouble(2, easinessFactor);
            stmt.setString(3, lastReviewedAt);
            stmt.setString(4, nextReviewAt);
            stmt.setInt(5, flashcardId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves flashcards from a deck that are due for review.
     *
     * @param deckId the ID of the deck
     * @return a list of due Flashcard objects
     */
    public static List<Flashcard> getDueFlashcards(int deckId) {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE deck_id = ? AND " +
                "(next_review_at IS NULL OR next_review_at <= date('now'))";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Flashcard card = new Flashcard(
                        rs.getInt("id"),
                        rs.getInt("deck_id"),
                        rs.getString("front"),
                        rs.getString("back"),
                        rs.getString("media_type"),
                        rs.getString("difficulty"),
                        rs.getString("created_at"),
                        rs.getString("image_path"),
                        rs.getInt("repetitions"),
                        rs.getDouble("easiness_factor"),
                        rs.getString("last_reviewed_at"),
                        rs.getString("next_review_at")
                );
                flashcards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Fetch due flashcards failed: " + e.getMessage());
        }
        return flashcards;
    }

    public static int countReviewedFlashcards(int userId) {
        String sql = """
        SELECT COUNT(*) FROM flashcards f
        JOIN decks d ON f.deck_id = d.id
        WHERE d.user_id = ? AND f.last_reviewed_at IS NOT NULL
    """;

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Failed to count reviewed flashcards: " + e.getMessage());
        }
        return 0;
    }

}