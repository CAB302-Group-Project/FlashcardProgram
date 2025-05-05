package db;

import db.DAO.StudyMetricsDAO;

public class MetricsService
{

    /**
     * Resets all study metrics for the given user ID.
     * @param userId the user to reset
     * @return true if successful, false otherwise
     */
    public static boolean resetUserMetrics(int userId)
    {
        return StudyMetricsDAO.resetMetricsForUser(userId);
    }
}
