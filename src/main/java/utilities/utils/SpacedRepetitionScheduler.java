package utilities.utils;

import db.Flashcard;
import java.time.LocalDate;

/**
 * Implements a spaced repetition algorithm to schedule flashcard reviews.
 * <p>
 * This class uses a variation of the SM-2 algorithm to calculate optimal review intervals
 * based on the difficulty rating provided by the user. The scheduling adapts to the user's
 * performance by adjusting the easiness factor of each flashcard.
 * </p>
 *
 * @author Della Rebu
 * @version 1.0
 * @see Flashcard
 * @since 1.0
 */
public class SpacedRepetitionScheduler {

    /** Interval in days for cards rated as easy (1 week) */
    private static final int EASY_INTERVAL = 7;

    /** Interval in days for cards rated as medium (~twice a week) */
    private static final int MEDIUM_INTERVAL = 3;

    /** Interval in days for cards rated as hard (daily) */
    private static final int HARD_INTERVAL = 1;

    /** Adjustment factor for easy-rated cards */
    private static final double EASY_EF_CHANGE = 0.1;

    /** Adjustment factor for hard-rated cards */
    private static final double HARD_EF_CHANGE = -0.15;

    /** Minimum allowable easiness factor */
    private static final double MIN_EF = 1.3;

    /**
     * Schedules the next review date for a flashcard based on user-rated difficulty.
     * <p>
     * This method:
     * <ol>
     *   <li>Updates the easiness factor based on the difficulty rating</li>
     *   <li>Calculates the appropriate review interval</li>
     *   <li>Updates all flashcard review parameters</li>
     *   <li>Adjusts the flashcard's difficulty level based on performance</li>
     * </ol>
     * </p>
     *
     * @param flashcard the flashcard to schedule (cannot be null)
     * @param difficulty the user-rated difficulty ("Easy", "Medium", or "Hard")
     * @throws IllegalArgumentException if difficulty is not one of the expected values
     * @throws NullPointerException if flashcard is null
     */
    public static void scheduleNextReview(Flashcard flashcard, String difficulty) {
        // Validate parameters
        if (flashcard == null) {
            throw new NullPointerException("Flashcard cannot be null");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        int repetitions = flashcard.getRepetitions();
        double easinessFactor = flashcard.getEasinessFactor();

        // Update easiness factor based on difficulty
        switch (difficulty.toLowerCase()) {
            case "easy":
                easinessFactor += EASY_EF_CHANGE;
                break;
            case "medium":
                // No change to EF for medium difficulty
                break;
            case "hard":
                easinessFactor = Math.max(MIN_EF, easinessFactor + HARD_EF_CHANGE);
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }

        // Calculate interval based on difficulty
        int interval;
        switch (difficulty.toLowerCase()) {
            case "easy":
                interval = EASY_INTERVAL;
                break;
            case "medium":
                interval = MEDIUM_INTERVAL;
                break;
            case "hard":
                interval = HARD_INTERVAL;
                break;
            default:
                interval = MEDIUM_INTERVAL; // fallback
        }

        // Adjust interval based on current easiness factor
        interval = (int) Math.round(interval * (2.5 / easinessFactor));

        // Ensure minimum interval of 1 day
        interval = Math.max(1, interval);

        // Update flashcard properties
        flashcard.setRepetitions(repetitions + 1);
        flashcard.setEasinessFactor(easinessFactor);
        flashcard.setLastReviewedAt(LocalDate.now().toString());
        flashcard.setNextReviewAt(LocalDate.now().plusDays(interval).toString());

        // Adjust difficulty level based on EF
        if (easinessFactor >= 2.0) {
            flashcard.setDifficulty("Easy");
        } else if (easinessFactor >= 1.5) {
            flashcard.setDifficulty("Medium");
        } else {
            flashcard.setDifficulty("Hard");
        }
    }
}