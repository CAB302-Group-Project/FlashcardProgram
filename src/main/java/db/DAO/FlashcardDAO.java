package db.DAO;

import db.DBConnector;
import db.Flashcard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDAO
{

    // Insert a flashcard (with optional image)
    public static boolean insertFlashcard(int deckId, String front, String back, String mediaType, String difficulty, String imagePath)
    {
        String sql = "INSERT INTO flashcards(deck_id, front, back, media_type, difficulty, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            pstmt.setString(2, front);
            pstmt.setString(3, back);
            pstmt.setString(4, mediaType);
            pstmt.setString(5, difficulty);
            pstmt.setString(6, imagePath); // can be null or relative path
            pstmt.executeUpdate();
            return true;

        }
        catch (SQLException e)
        {
            System.err.println("Insert flashcard failed: " + e.getMessage());
            return false;
        }
    }

    // Get all flashcards for a deck
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
                        rs.getString("image_path")
                );
                flashcards.add(card);
            }

        }
        catch (SQLException e) {
            System.err.println("Fetch flashcards failed: " + e.getMessage());
        }

        return flashcards;
    }

    // Get a single flashcard by ID
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
                        rs.getString("image_path")
                );
            }

        }
        catch (SQLException e) {
            System.err.println("Get flashcard by ID failed: " + e.getMessage());
        }

        return null;
    }

    // Update flashcard
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

    // Delete a flashcard
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

    public static boolean insertFlashcard(int deckId, String front, String back) {
        String sql = "INSERT INTO flashcards(deck_id, front, back) VALUES (?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            stmt.setString(2, front);
            stmt.setString(3, back);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
