package db;

import db.DAO.StudyMetricsDAO;

/**
 * Service class that provides higher-level operations on study metrics.
 * Acts as a wrapper around {@link StudyMetricsDAO} for easier external access.
 */
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
