package utilities.controllers;

import utilities.models.Deck;
import utilities.models.Flashcard;
import utilities.models.QuizSession;
import utilities.services.ApiService;
import utilities.services.PerformanceTracker;
import utilities.services.QuizLogger;
import utilities.utils.TimerUtil;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class QuizController {
    private final ApiService apiService;
    private final PerformanceTracker tracker;
    private final TimerUtil timer;
    private final QuizLogger quizLogger;

    public QuizController() {
        this.apiService = new ApiService();
        this.tracker = new PerformanceTracker();
        this.timer = new TimerUtil();
        this.quizLogger = QuizLogger.getInstance();
    }

    public void startQuiz(String deckId) {
        Deck deck = apiService.fetchDeckById(deckId); // You'll need to implement this
        if (deck == null || deck.getFlashcards() == null || deck.getFlashcards().isEmpty()) {
            System.out.println("Deck not found or empty! Debug: " +
                    (deck == null ? "null deck" :
                            (deck.getFlashcards() == null ? "null flashcards" :
                                    "empty flashcards")));
            return;
        }

        System.out.println("Starting quiz for: " + deck.getName());
        timer.start();

        Scanner scanner = new Scanner(System.in);
        int correctAnswers = 0;
        int questionsAttempted = 0;

        // In the question handling loop:
        for (Flashcard card : deck.getFlashcards()) {
            if (card.getNextReviewDate() == null ||
                    !LocalDate.now().isBefore(card.getNextReviewDate())) {

                System.out.println("\nQuestion: " + card.getQuestion());
                System.out.print("Your answer: ");
                String userAnswer = scanner.nextLine();
                questionsAttempted++;

                boolean isCorrect = userAnswer.trim().equalsIgnoreCase(card.getAnswer().trim());
                tracker.recordAttempt(card, isCorrect);

                if (isCorrect) {
                    correctAnswers++;
                    System.out.println("✓ Correct!");
                } else {
                    System.out.println("✗ Incorrect. Answer: " + card.getAnswer());
                }

                // ADD THIS TO SHOW REVIEW SCHEDULE
                String reviewMessage = card.getNextReviewDate().equals(LocalDate.now().plusDays(1))
                        ? "Next review: tomorrow"
                        : "Next review in: " + ChronoUnit.DAYS.between(LocalDate.now(),
                        card.getNextReviewDate()) + " days";
                System.out.println(reviewMessage);
            } else {
                System.out.println("\n[Card not due for review until: " +
                        card.getNextReviewDate() + "]");
            }
        }

        timer.pause();
        QuizSession session = new QuizSession(
                deckId,
                correctAnswers,
                questionsAttempted,
                timer.getElapsedTimeMillis()
        );
        quizLogger.logSession(session);
        displayQuizResults(deck, correctAnswers, questionsAttempted); // New results method
    }
    public void showSessionHistory() {
        System.out.println("\n=== Quiz Session History ===");
        quizLogger.getQuizLog().getSessions().forEach(session -> {
            System.out.printf(
                    "%s - Deck: %s | Score: %d/%d (%.1f%%) | Time: %d sec",
                    session.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
                    session.getDeckId(),
                    session.getCorrectAnswers(),
                    session.getTotalQuestions(),
                    session.getPercentage(),
                    session.getDurationMillis() / 1000
            );
        });
    }

    // Add this new method to show scoring
    private void displayQuizResults(Deck deck, int correctAnswers, int questionsAttempted) {
        //double percentage = (correctAnswers * 100.0) / totalAttempted;
        double percentage = questionsAttempted > 0
                ? (correctAnswers * 100.0) / questionsAttempted
                : 0;

        System.out.println("\n=== Quiz Results ===");
        System.out.printf("Score: %d/%d (%.1f%%)%n", correctAnswers, questionsAttempted, percentage);
        System.out.println("Time: " + timer.getFormattedTime());

        if (percentage >= 80) {
            System.out.println("🎉 Excellent work!");
        } else if (percentage >= 50) {
            System.out.println("👍 Good effort!");
        } else {
            System.out.println("💪 Keep practicing!");
        }
    }
}
