package utilities.services;

import utilities.models.Deck;
import utilities.models.Flashcard;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    public List<Deck> fetchDecks() {
        // TODO: Implement actual API call
        // Mock implementation for now:
        return List.of(
                new Deck("1", "Java Basics", "Basic Java concepts"),
                new Deck("2", "Spring Framework", "Spring Boot fundamentals")
        );
    }
    /*public Deck fetchDeckById(String id) {
        // Mock implementation - replace with real API call
        return fetchDecks().stream()
                .filter(deck -> deck.getId().equals(id))
                .findFirst()
                .orElse(null);
    }*/
    public Deck fetchDeckById(String id) {
        // Create a deck with sample flashcards
        Deck deck = new Deck(id, "Java Basics", "Basic Java concepts");

        // Add sample flashcards
        List<Flashcard> flashcards = new ArrayList<>();
        flashcards.add(new Flashcard("1", "What is JVM?", "Java Virtual Machine"));
        flashcards.add(new Flashcard("2", "What is JRE?", "Java Runtime Environment"));
        flashcards.add(new Flashcard("3", "What is JDK?", "Java Development Kit"));

        deck.setFlashcards(flashcards);
        return deck;
    }

}