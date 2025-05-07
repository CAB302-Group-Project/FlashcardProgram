/*package com.example.flashcardai.utils;

public class SpacedRepetition {
    public static int calculateNextInterval(int previousInterval, int performanceRating) {
        if (performanceRating < 3) return 1;
        return (int) (previousInterval * 1.5);
    }
}*/

package utilities.utils;

import utilities.models.Flashcard;
import java.time.LocalDate;

public class SpacedRepetition {
    private static final double INITIAL_EASE_FACTOR = 2.5;
    private static final int FIRST_INTERVAL = 1; // days
    private static final int SECOND_INTERVAL = 6; // days

    public static void applySpacedRepetition(Flashcard card, boolean answerCorrect) {
        // Update ease factor first
        card.setEaseFactor(updateEaseFactor(card.getEaseFactor(), answerCorrect));

        // Calculate next review date
        int interval = calculateInterval(card.getRepetitions(), card.getEaseFactor());
        card.setNextReviewDate(LocalDate.now().plusDays(interval));

        // Increment repetition count if answered correctly
        if (answerCorrect) {
            card.setRepetitions(card.getRepetitions() + 1);
        } else {
            card.setRepetitions(0); // Reset if answered wrong
        }
    }

    private static int calculateInterval(int repetitions, double easeFactor) {
        if (repetitions == 0) return FIRST_INTERVAL;
        if (repetitions == 1) return SECOND_INTERVAL;
        return (int) Math.round((repetitions - 1) * easeFactor);
    }

    private static double updateEaseFactor(double currentEase, boolean answerCorrect) {
        double newEase = answerCorrect
                ? currentEase + 0.1
                : currentEase - 0.15;
        return Math.max(1.3, newEase); // Never go below 1.3
    }
}

/*public class SpacedRepetition {
    public static LocalDate calculateNextReview(
            int repetitions,
            double easeFactor,
            int previousInterval
    ) {
        if (repetitions == 0) {
            return LocalDate.now().plusDays(1); // First repetition: next day
        } else if (repetitions == 1) {
            return LocalDate.now().plusDays(6); // Second repetition: 6 days
        } else {
            int interval = (int) Math.ceil(previousInterval * easeFactor);
            return LocalDate.now().plusDays(interval);
        }
    }

    public static double updateEaseFactor(double currentEase, boolean answerCorrect) {
        if (answerCorrect) {
            return Math.max(1.3, currentEase + 0.1); // Cap ease factor at minimum 1.3
        } else {
            return Math.max(1.3, currentEase - 0.15); // Reduce but don't go below 1.3
        }
    }
}*/
