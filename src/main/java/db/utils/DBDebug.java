package db.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import db.DAO.DeckDAO;
import db.DAO.FlashcardDAO;
import db.DBConnector;
import db.Deck;
import db.Flashcard;

/**
 * Utility class for performing database debugging operations.
 * Includes methods to clear all data and print contents for testing.
 */
public class DBDebug
{
    /**
     * Clears all data from the database.
     * Disables foreign key constraints to safely delete dependent rows.
     */
    public static void clearDatabase()
    {
        try (Connection conn = DBConnector.connect();
             Statement stmt = conn.createStatement())
        {

            // Disable foreign key checks temporarily
            stmt.execute("PRAGMA foreign_keys = OFF;");

            // Clear tables (order matters if foreign keys are enabled)
            stmt.execute("DELETE FROM study_metrics;");
            stmt.execute("DELETE FROM flashcards;");
            stmt.execute("DELETE FROM decks;");
            stmt.execute("DELETE FROM users;");

            // Re-enable foreign key checks
            stmt.execute("PRAGMA foreign_keys = ON;");

            System.out.println("Database cleared successfully.");

        } catch (Exception e)
        {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }

    /**
     * Prints all decks for a test user (user ID 1).
     * Replace with desired user ID as needed.
     */
    public static void printAllDecks()
    {
        List<Deck> decks = DeckDAO.getDecksByUserId(1); // replace with any test user
        decks.forEach(System.out::println);
    }

    /**
     * Prints all flashcards for a given deck ID.
     *
     * @param deckId the ID of the deck
     */
    public static void printAllFlashcards(int deckId)
    {
        List<Flashcard> cards = FlashcardDAO.getFlashcardsByDeckId(deckId);
        cards.forEach(System.out::println);
    }

}
