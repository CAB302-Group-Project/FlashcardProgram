package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String DB_PATH = "data/flashcards.db"; // Will be created automatically

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + DB_PATH;
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver manually loaded!");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver NOT found: " + e.getMessage());
        }
        return conn;
    }
}
