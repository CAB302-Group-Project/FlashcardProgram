package com.example.flashcardai.services;

import java.util.HashMap;

public class AuthService {
    private static final HashMap<String, String> users = new HashMap<>();

    public static boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password); // Replace with hash in real app
        return true;
    }

    public static boolean login(String username, String password) {
        return users.getOrDefault(username, "").equals(password);
    }
}
