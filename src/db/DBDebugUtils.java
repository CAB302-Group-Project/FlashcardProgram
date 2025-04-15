package db;

import java.sql.Connection;
import java.sql.Statement;

public class DBDebugUtils {

    public static void clearDatabase() {
        try (Connection conn = DBConnector.connect();
             Statement stmt = conn.createStatement()) {

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

        } catch (Exception e) {
            System.err.println("Failed to clear database: " + e.getMessage());
        }
    }
}
