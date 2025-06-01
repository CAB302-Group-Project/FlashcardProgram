package db;

import java.util.List;

/**
 * Model class representing a deck of flashcards.
 * A deck belongs to a user and contains metadata like title, description, and creation timestamp.
 *
 * <p>This class is typically used for displaying and managing user decks in the flashcard app.</p>
 *
 *
 */
public class Deck
{
    private final int id;
    private final int userId;
    private final String title;
    private final String description;
    private final String createdAt;

    /**
     * Constructs a new Deck instance.
     *
     * @param id          the unique ID of the deck
     * @param userId      the ID of the user who owns the deck
     * @param title       the title of the deck
     * @param description the description of the deck
     * @param createdAt   the creation timestamp of the deck
     */
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

    // Getters
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
