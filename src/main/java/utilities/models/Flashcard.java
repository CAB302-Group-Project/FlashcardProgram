/*package com.example.flashcardai.models;

public class Flashcard {
    private String id;
    private String deckId;
    private String front;
    private String back;
    private int easeFactor;
    private int interval;

    public Flashcard(String id, String deckId, String front, String back) {
        this.id = id;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
        this.easeFactor = 250; // default EF
        this.interval = 1;
    }

    // Getters and setters
}*/

package utilities.models;
import java.time.LocalDate;

public class Flashcard {
    private String id;
    private String question;
    private String answer;
    private LocalDate nextReviewDate;
    private int repetitions;
    private double easeFactor;

    // Constructor
    public Flashcard(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    // Getters (MUST HAVE THESE EXACT METHODS)
    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    // Setters (optional)
    public void setId(String id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double getEaseFactor() {
        return easeFactor;
    }

    public void setEaseFactor(double easeFactor) {
        this.easeFactor = easeFactor;
    }
}