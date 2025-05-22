/*package utilities.utils;

import db.DBConnector;
import db.Flashcard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import java.time.LocalDate;

public class SpacedRepetitionScheduler {

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
}*/
