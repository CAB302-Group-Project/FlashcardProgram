/*package com.example.flashcardai.models;

public class Deck {
    private String id;
    private String userId;
    private String name;
    private String category;

    public Deck(String id, String userId, String name, String category) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.category = category;
    }

    // Getters and setters
}*/

package com.example.flashcardai.models;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private String id;
    private String name;
    private String description;
    private List<Flashcard> flashcards = new ArrayList<>();

    // Constructors
    public Deck() {}

    public Deck(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}