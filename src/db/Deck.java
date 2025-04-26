package db;

public class Deck {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String createdAt;

    public Deck(int id, int userId, String title, String description, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + description + " (" + createdAt + ")";
    }
}
