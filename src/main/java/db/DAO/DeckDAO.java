package db.DAO;

import app.FlashcardApp;
import db.DBConnector;
import db.Deck;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckDAO {

    // Insert a new deck
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

    // Get all decks for a user
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

    // Get deck by ID
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

    // Rename deck
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

    // Delete deck
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

}
