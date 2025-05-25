package db.DAO;

import db.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing study metrics related to flashcard reviews.
 * Tracks user performance on each flashcard and supports spaced repetition strategies.
 *
 * <p>This class supports recording review attempts, identifying difficult cards,
 * and resetting all metrics for a given user. It aligns with spaced repetition
 * functionality described in the project’s user stories.</p>
 *
 *
 */
public class StudyMetricsDAO
{

    /**
     * Records a user's review result for a specific flashcard.
     * If the record exists, it updates it; otherwise, it inserts a new entry.
     *
     * @param userId      the ID of the user
     * @param flashcardId the ID of the reviewed flashcard
     * @param result      the result of the review ("easy", "hard", or "incorrect")
     * @return true if the operation was successful; false otherwise
     * @throws IllegalArgumentException if result is not one of the allowed values
     */
    public static boolean recordReview(int userId, int flashcardId, String result)
    {
        if (!result.equals("easy") && !result.equals("hard") && !result.equals("incorrect"))
        {
            throw new IllegalArgumentException("Result must be 'easy', 'hard', or 'incorrect'");
        }

        String selectSql = "SELECT * FROM study_metrics WHERE user_id = ? AND flashcard_id = ?";
        String insertSql = "INSERT INTO study_metrics (user_id, flashcard_id, last_result, times_practiced, times_easy, times_hard, times_incorrect, last_reviewed) " +
                "VALUES (?, ?, ?, 1, ?, ?, ?, CURRENT_TIMESTAMP)";
        String updateSql = "UPDATE study_metrics SET " +
                "times_practiced = times_practiced + 1, " +
                "last_result = ?, " +
                "last_reviewed = CURRENT_TIMESTAMP, " +
                "times_easy = times_easy + ?, " +
                "times_hard = times_hard + ?, " +
                "times_incorrect = times_incorrect + ? " +
                "WHERE user_id = ? AND flashcard_id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement checkStmt = conn.prepareStatement(selectSql))
        {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, flashcardId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next())
            {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql))
                {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, flashcardId);
                    insertStmt.setString(3, result);
                    insertStmt.setInt(4, result.equals("easy") ? 1 : 0);
                    insertStmt.setInt(5, result.equals("hard") ? 1 : 0);
                    insertStmt.setInt(6, result.equals("incorrect") ? 1 : 0);
                    insertStmt.executeUpdate();
                }
            }
            else
            {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, result);
                    updateStmt.setInt(2, result.equals("easy") ? 1 : 0);
                    updateStmt.setInt(3, result.equals("hard") ? 1 : 0);
                    updateStmt.setInt(4, result.equals("incorrect") ? 1 : 0);
                    updateStmt.setInt(5, userId);
                    updateStmt.setInt(6, flashcardId);
                    updateStmt.executeUpdate();
                }
            }

            return true;

        }
        catch (SQLException e)
        {
            System.err.println("Failed to record review: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of flashcard IDs that the user has most difficulty with.
     * The difficulty score is calculated as: (hard + 2 × incorrect) / total attempts.
     *
     * @param userId the ID of the user
     * @param limit  the maximum number of flashcard IDs to return
     * @return a list of flashcard IDs sorted by difficulty score (descending)
     */
    public static List<Integer> getChallengingCardIds(int userId, int limit)
    {
        String sql = """
                SELECT flashcard_id,
                       times_practiced,
                       times_easy,
                       times_hard,
                       times_incorrect,
                       (1.0 * times_hard + 2.0 * times_incorrect) / times_practiced AS difficulty_score
                FROM study_metrics
                WHERE user_id = ?
                ORDER BY difficulty_score DESC
                LIMIT ?
                """;

        List<Integer> cardIds = new ArrayList<>();

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cardIds.add(rs.getInt("flashcard_id"));
            }

        } catch (SQLException e)
        {
            System.err.println("Failed to fetch difficult cards: " + e.getMessage());
        }

        return cardIds;
    }

    /**
     * Deletes all study metrics for a given user, effectively resetting their learning history.
     * This supports the user story: “I want a way to reset my metrics so I can start fresh.”
     *
     * @param userId the ID of the user whose metrics are to be cleared
     * @return true if the reset operation was successful; false otherwise
     */
    public static boolean resetMetricsForUser(int userId)
    {
        String sql = "DELETE FROM study_metrics WHERE user_id = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e)
        {
            System.err.println("Failed to reset metrics: " + e.getMessage());
            return false;
        }
    }
}
