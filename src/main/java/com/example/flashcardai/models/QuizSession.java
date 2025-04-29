package com.example.flashcardai.models;
import com.example.flashcardai.services.QuizLogger;

import java.time.LocalDateTime;

public class QuizSession {
    private String deckId;
    private LocalDateTime timestamp;
    private int correctAnswers;
    private int totalQuestions;
    private long durationMillis;

    // Constructor
    public QuizSession(String deckId, int correctAnswers, int totalQuestions, long durationMillis) {
        this.deckId = deckId;
        this.timestamp = LocalDateTime.now();
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.durationMillis = durationMillis;
    }

    // Getters
    public String getDeckId() { return deckId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getTotalQuestions() { return totalQuestions; }
    public long getDurationMillis() { return durationMillis; }

    // Utility method
    public double getPercentage() {
        return (correctAnswers * 100.0) / totalQuestions;
    }
}