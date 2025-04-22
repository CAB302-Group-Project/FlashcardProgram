package db;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit
{
    public static void main(String[] args) {
        try (Connection conn = DBConnector.connect();
             Statement stmt = conn.createStatement())
        {

            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);

            // Decks table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS decks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                );
            """);

            // Flashcards table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flashcards (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    deck_id INTEGER NOT NULL,
                    front TEXT NOT NULL,
                    back TEXT NOT NULL,
                    media_type TEXT DEFAULT 'text',
                    difficulty TEXT DEFAULT 'medium',
                    image_path TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
                );
            """);

            // Study Metrics table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS study_metrics (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    flashcard_id INTEGER NOT NULL,
                    times_practiced INTEGER DEFAULT 0,
                    times_easy INTEGER DEFAULT 0,
                    times_hard INTEGER DEFAULT 0,
                    times_incorrect INTEGER DEFAULT 0,
                    last_result TEXT CHECK(last_result IN ('incorrect', 'hard', 'easy')),
                    last_reviewed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (flashcard_id) REFERENCES flashcards(id) ON DELETE CASCADE,
                    UNIQUE(user_id, flashcard_id)
                );
            """);

            System.out.println("All tables created successfully!");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
