package utilities.services;

import utilities.models.Flashcard;
import utilities.utils.SpacedRepetition;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PerformanceTracker {
    private final Map<String, Flashcard> performanceData;

    public PerformanceTracker() {
        this.performanceData = new HashMap<>();
    }

    public void recordAttempt(Flashcard card, boolean wasCorrect) {
        // Initialize card stats if first attempt
        if (card.getEaseFactor() == 0) {
            card.setEaseFactor(2.5);
            card.setRepetitions(0);
        }

        // Apply spaced repetition
        SpacedRepetition.applySpacedRepetition(card, wasCorrect);

        // Store updated card
        performanceData.put(card.getId(), card);
    }

    public LocalDate getNextReviewDate(String cardId) {
        return performanceData.containsKey(cardId)
                ? performanceData.get(cardId).getNextReviewDate()
                : LocalDate.now();
    }

    private static class CardPerformance {
        int repetitions;
        double easeFactor;
        int interval;
        LocalDate nextReview;

        public CardPerformance(int repetitions, double easeFactor, int interval) {
            this.repetitions = repetitions;
            this.easeFactor = easeFactor;
            this.interval = interval;
            this.nextReview = LocalDate.now();
        }
    }
}