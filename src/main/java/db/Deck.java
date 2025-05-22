package db;

import java.util.List;

public class Deck
{
    private final int id;
    private final int userId;
    private final String title;
    private final String description;
    private final String createdAt;

    public Deck(int id, int userId, String title, String description, String createdAt)
    {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public class DeckDataHolder {
        public static List<String> questions;
        public static List<String> answers;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString()
    {
        return "[" + id + "] " + title + " - " + description + " (" + createdAt + ")";
    }
}
