package com.example.flashcardai.models;

public class User {
    private String id;
    private String username;
    private String passwordHash;

    public User(String id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getters and setters
}
