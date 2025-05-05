package db.DAO;

import app.FlashcardApp;
import db.DBConnector;
import db.Deck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckDAO {

    // Insert a new deck
    public static boolean insertDeck(int userId, String title, String description)
    {
        String sql = "INSERT INTO decks(user_id, title, description) VALUES (?, ?, ?)";
        Connection conn = FlashcardApp.getInstance().getDBConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.executeUpdate();
            return true;

        }
        catch (SQLException e)
        {
            System.err.println("Insert deck failed: " + e.getMessage());
            return false;
        }
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
}
