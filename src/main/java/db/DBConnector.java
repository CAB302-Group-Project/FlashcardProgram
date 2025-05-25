package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for managing SQLite database connections.
 * Chooses between test and production databases using the "db.name" system property.
 *
 * <p>If no property is set, it defaults to "flashcards.db".</p>
 *
 * Example usage:
 * <pre>
 *     Connection conn = DBConnector.connect();
 * </pre>
 *
 *
 */
public class DBConnector {

    /**
     * Establishes a connection to the SQLite database.
     * Defaults to "flashcards.db" unless overridden via system property "db.name".
     *
     * @return a valid {@link Connection} object, or null if the connection fails
     */
    public static Connection connect() {
        try {
            // Use test DB if specified, otherwise default to production DB
            String dbName = System.getProperty("db.name", "flashcards.db");
            String url = "jdbc:sqlite:" + dbName;

            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite: " + dbName);
            return conn;

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
