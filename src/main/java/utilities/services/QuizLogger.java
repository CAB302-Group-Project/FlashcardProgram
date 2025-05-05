package utilities.services;

import utilities.models.QuizLog;
import utilities.models.QuizSession;

public class QuizLogger {
    private static final QuizLog quizLog = new QuizLog(); // Now static

    private QuizLogger() {} // Private constructor

    public static QuizLogger getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final QuizLogger INSTANCE = new QuizLogger();
    }
    //private final QuizLog quizLog = new QuizLog();

    public void logSession(QuizSession session) {
        quizLog.addSession(session);
        // In a real app, you'd also persist to database/file here
        //System.out.println("\n[Logged session] " + sessionToString(session));
    }

    public QuizLog getQuizLog() {
        return quizLog;
    }

    private String sessionToString(QuizSession session) {
        return String.format(
                "Deck: %s | Score: %d/%d (%.1f%%) | Time: %d sec",
                session.getDeckId(),
                session.getCorrectAnswers(),
                session.getTotalQuestions(),
                session.getPercentage(),
                session.getDurationMillis() / 1000
        );
    }
}