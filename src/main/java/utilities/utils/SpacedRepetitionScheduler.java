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
 * @author [Della Rebu]
 * @version 1.0
 * @see Flashcard
 * @since 1.0
 */

public class SpacedRepetitionScheduler {
    /**
     * Schedules the next review date for a flashcard based on the specified difficulty.
     * <p>
     * This method updates the flashcard's properties including:
     * <ul>
     *   <li>Repetition count</li>
     *   <li>Easiness factor (EF)</li>
     *   <li>Last reviewed date</li>
     *   <li>Next review date</li>
     * </ul>
     * </p>
     *
     * @param flashcard the flashcard to schedule (cannot be null)
     * @param difficulty the difficulty rating of the current review session.
     *        Must be one of: "easy", "medium", or "hard" (case-insensitive)
     * @throws IllegalArgumentException if difficulty is not one of the expected values
     * @throws NullPointerException if flashcard or difficulty is null
     *
     * @see Flashcard#getRepetitions()
     * @see Flashcard#getEasinessFactor()
     * @see Flashcard#setRepetitions(int)
     * @see Flashcard#setEasinessFactor(double)
     * @see Flashcard#setLastReviewedAt(String)
     * @see Flashcard#setNextReviewAt(String)
     */
    public static void scheduleNextReview(Flashcard flashcard, String difficulty) {
        int repetitions = flashcard.getRepetitions();
        double easinessFactor = flashcard.getEasinessFactor();

        // Update easiness factor
        switch (difficulty.toLowerCase()) {
            case "easy":
                easinessFactor += 0.1;
                break;
            case "medium":
                // No change
                break;
            case "hard":
                easinessFactor = Math.max(1.3, easinessFactor - 0.15);
                break;
        }

        // Calculate interval
        int interval;
        if (repetitions == 0) {
            interval = 1; // First review tomorrow
        } else if (repetitions == 1) {
            interval = 3; // Second review in 3 days
        } else {
            interval = (int) Math.round((repetitions - 1) * easinessFactor);
        }

        // Update repetitions
        repetitions++;

        // Update flashcard
        flashcard.setRepetitions(repetitions);
        flashcard.setEasinessFactor(easinessFactor);
        flashcard.setLastReviewedAt(LocalDate.now().toString());
        flashcard.setNextReviewAt(LocalDate.now().plusDays(interval).toString());
    }
}
