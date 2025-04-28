package com.example.flashcardai.controllers;

import com.example.flashcardai.models.Deck;  // Fixed import
import com.example.flashcardai.services.ApiService;
import java.util.List;

public class DashboardController {
    private final ApiService apiService;

    public DashboardController() {
        this.apiService = new ApiService();
    }

    public void displayDecks() {
        List<Deck> decks = apiService.fetchDecks();  // Fix this typo (Deck instead of Deck)
        System.out.println("Available Flashcard Decks:");
        decks.forEach(deck -> System.out.println("- " + deck.getName() + ": " + deck.getDescription()));
    }
}