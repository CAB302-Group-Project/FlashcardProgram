package db.DAO;

import db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PomoDao {

    public static void logSession(int userId) {
        String sql = "INSERT INTO pomodoro_log (user_id) VALUES (?)";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log Pomodoro session: " + e.getMessage());
        }
    }

    public static int countSessionsThisWeek(int userId) {
        String sql = """
            SELECT COUNT(*) FROM pomodoro_log
            WHERE user_id = ? AND timestamp >= datetime('now', 'start of day', '-6 days')
        """;
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Failed to count Pomodoro sessions: " + e.getMessage());
        }
        return 0;
    }
}
