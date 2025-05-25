package db.DAO;

import app.FlashcardApp;
import db.DBConnector;
import db.Deck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for performing CRUD operations on the 'decks' table.
 * Provides methods for inserting, retrieving, updating, and deleting flashcard decks.
 * Also supports creating test decks and retrieving decks with flashcards for test data purposes.
 *
 * <p>This class relies on {@link DBConnector} for database connections and operates on {@link Deck} objects.</p>
 *
 *
 */

public class DeckDAO {

    /**
     * Inserts a new deck for a specified user.
     *
     * @param userId     the ID of the user creating the deck
     * @param title      the title of the deck
     * @param description a brief description of the deck
     * @return the generated deck ID if successful; -1 otherwise
     */
    public static int insertDeck(int userId, String title, String description)
    {
        String sql = "INSERT INTO decks(user_id, title, description) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, description);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return -1;

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all decks belonging to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Deck} objects associated with the user
     */
    public static List<Deck> getDecksByUserId(int userId)
    {
        List<Deck> decks = new ArrayList<>();
        String sql = "SELECT * FROM decks WHERE user_id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                decks.add(new Deck(
                        rs.getInt("id"),
                        userId,
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("created_at")
                ));
            }

        } catch (SQLException e)
        {
            System.err.println("Get decks failed: " + e.getMessage());
        }

        return decks;
    }

    /**
     * Retrieves a single deck by its ID.
     *
     * @param deckId the ID of the deck
     * @return a {@link Deck} object if found; null otherwise
     */
    public static Deck getDeckById(int deckId)
    {
        String sql = "SELECT * FROM decks WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Deck(
                        deckId,
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("created_at")
                );
            }

        }
        catch (SQLException e)
        {
            System.err.println("Get deck by ID failed: " + e.getMessage());
        }

        return null;
    }

    /**
     * Renames an existing deck and updates its description.
     *
     * @param deckId   the ID of the deck to update
     * @param newTitle the new title for the deck
     * @param newDesc  the new description for the deck
     * @return true if the update was successful; false otherwise
     */

    public static boolean renameDeck(int deckId, String newTitle, String newDesc)
    {
        String sql = "UPDATE decks SET title = ?, description = ? WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, newTitle);
            stmt.setString(2, newDesc);
            stmt.setInt(3, deckId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e)
        {
            System.err.println("Rename deck failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a deck by its ID.
     *
     * @param deckId the ID of the deck to delete
     * @return true if the deletion was successful; false otherwise
     */
    public static boolean deleteDeck(int deckId)
    {
        String sql = "DELETE FROM decks WHERE id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, deckId);
            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e)
        {
            System.err.println("Delete deck failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all decks in the system. Intended for administrative or testing purposes.
     *
     * @return an array of all {@link Deck} objects in the database
     */
    public static Deck[] getAllDecks() {
        String sql = "SELECT * FROM decks";
        List<Deck> decks = new ArrayList<>();
        try {
            Connection conn = FlashcardApp.getInstance().getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String created_at = rs.getString("created_at");
                Deck deck = new Deck(id, userId, title, description, created_at);
                decks.add(deck);
            }
        } catch (SQLException e) {
            System.err.println("Get all decks failed: " + e.getMessage());
        }

        Deck[] decksArray = new Deck[decks.size()];
        return decks.toArray(decksArray);
    }

    /**
     * Inserts a test deck and pre-defined flashcards for a given user.
     * This is useful for populating demo or debug environments.
     *
     * @param userId the ID of the user who will own the test deck
     */
    public static void insertTestDeckWithCards(int userId) {
        String insertDeckSQL = "INSERT INTO decks(user_id, title, description) VALUES (?, ?, ?)";
        String insertCardSQL = "INSERT INTO flashcards(deck_id, front, back, media_type, difficulty, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect()) {
            conn.setAutoCommit(false);

            // Insert deck
            int deckId;
            try (PreparedStatement deckStmt = conn.prepareStatement(insertDeckSQL, Statement.RETURN_GENERATED_KEYS)) {
                deckStmt.setInt(1, userId);
                deckStmt.setString(2, "Test Deck");
                deckStmt.setString(3, "This is a test deck with flashcards.");
                deckStmt.executeUpdate();

                try (ResultSet keys = deckStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        deckId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        throw new SQLException("Failed to retrieve deck ID.");
                    }
                }
            }

            // Insert test flashcards
            try (PreparedStatement cardStmt = conn.prepareStatement(insertCardSQL)) {
                Object[][] testCards = {
                        {"What is the capital of France?", "Paris", "text", "easy", null},
                        {"Solve: 9 * 12", "108", "text", "medium", null},
                        {"Explain polymorphism in OOP", "It allows different objects to respond to the same method in their own way.", "text", "hard", null}
                };

                for (Object[] card : testCards) {
                    cardStmt.setInt(1, deckId);
                    cardStmt.setString(2, (String) card[0]); // front
                    cardStmt.setString(3, (String) card[1]); // back
                    cardStmt.setString(4, (String) card[2]); // media_type
                    cardStmt.setString(5, (String) card[3]); // difficulty
                    cardStmt.setString(6, (String) card[4]); // image_path (nullable)
                    cardStmt.addBatch();
                }

                cardStmt.executeBatch();
            }

            conn.commit();
            System.out.println("Test deck and flashcards inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a deck and all associated flashcards by the deck's ID.
     * This operation is transactional to ensure consistency.
     *
     * @param deckId the ID of the deck to delete
     * @return true if the deck and its flashcards were successfully deleted; false otherwise
     */
    public static boolean deleteDeckById(int deckId) {
        String deleteCardsSQL = "DELETE FROM flashcards WHERE deck_id = ?";
        String deleteDeckSQL = "DELETE FROM decks WHERE id = ?";

        try (Connection conn = DBConnector.connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Delete flashcards first
            try (PreparedStatement deleteCardsStmt = conn.prepareStatement(deleteCardsSQL)) {
                deleteCardsStmt.setInt(1, deckId);
                deleteCardsStmt.executeUpdate();
            }

            // Delete the deck
            try (PreparedStatement deleteDeckStmt = conn.prepareStatement(deleteDeckSQL)) {
                deleteDeckStmt.setInt(1, deckId);
                int affectedRows = deleteDeckStmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // Commit both deletes
                    System.out.println("Deck deleted successfully.");
                    return true;
                } else {
                    conn.rollback(); // No deck deleted
                    System.out.println("Deck not found. Nothing was deleted.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Failed to delete deck: " + e.getMessage());
            return false;
        }
    }


}
