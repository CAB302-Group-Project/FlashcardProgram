package utilities.models;

import java.util.ArrayList;
import java.util.List;

public class QuizLog {
    private List<QuizSession> sessions = new ArrayList<>();

    public void addSession(QuizSession session) {
        sessions.add(session);
    }

    public List<QuizSession> getSessions() {
        return sessions;
    }

    public List<QuizSession> getSessionsByDeck(String deckId) {
        return sessions.stream()
                .filter(s -> s.getDeckId().equals(deckId))
                .toList();
    }
}