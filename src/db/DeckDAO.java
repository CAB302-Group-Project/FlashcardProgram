package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckDAO {

    // Create a new deck
    public static boolean insertDeck(int userId, String title, String description) {
        String sql = "INSERT INTO decks(user_id, title, description) VALUES (?, ?, ?)";

        try (Connection conn = db.DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Insert deck failed: " + e.getMessage());
            return false;
        }
    }

    // Get all decks for a user
    public static List<Deck> getDecksByUserId(int userId) {
        List<Deck> decks = new ArrayList<>();
        String sql = "SELECT id, title, description, created_at FROM decks WHERE user_id = ?";

        try (Connection conn = db.DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Deck deck = new Deck(
                        rs.getInt("id"),
                        userId,
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("created_at")
                );
                decks.add(deck);
            }

        } catch (SQLException e) {
            System.err.println("Get decks failed: " + e.getMessage());
        }

        return decks;
    }

    // Delete a deck by ID
    public static boolean deleteDeck(int deckId) {
        String sql = "DELETE FROM decks WHERE id = ?";

        try (Connection conn = db.DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Delete deck failed: " + e.getMessage());
            return false;
        }
    }
}
