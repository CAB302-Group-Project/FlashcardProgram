package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

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
