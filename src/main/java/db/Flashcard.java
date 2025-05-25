package db;

/**
 * Model class representing a flashcard.
 * Flashcards belong to a deck and include learning content along with spaced repetition metadata.
 *
 * <p>Supports both text and media formats, and stores difficulty and usage metrics
 * to enable adaptive learning via spaced repetition.</p>
 *
 *
 */
public class Flashcard
{
    private final int id;
    private final int deckId;
    private final String front;
    private final String back;
    private final String mediaType;
    private final String difficulty;
    private final String createdAt;
    private final String imagePath;
    private int repetitions;
    private double easinessFactor;
    private String lastReviewedAt;
    private String nextReviewAt;

    /**
     * Constructs a Flashcard with all properties.
     *
     * @param id              the ID of the flashcard
     * @param deckId          the ID of the associated deck
     * @param front           the front (question/prompt) text
     * @param back            the back (answer/explanation) text
     * @param mediaType       the type of media used ("text", "image", etc.)
     * @param difficulty      the difficulty level of the flashcard
     * @param createdAt       timestamp of when the flashcard was created
     * @param imagePath       path to the image file, if applicable
     * @param repetitions     how many times the card has been reviewed
     * @param easinessFactor  SuperMemo easiness factor used in spaced repetition
     * @param lastReviewedAt  timestamp of last review
     * @param nextReviewAt    timestamp of next recommended review
     */
    public Flashcard(int id, int deckId, String front, String back, String mediaType, String difficulty, String createdAt, String imagePath, int repetitions, double easinessFactor,
                     String lastReviewedAt, String nextReviewAt)
    {
        this.id = id;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
        this.mediaType = mediaType;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
        this.repetitions = repetitions;
        this.easinessFactor = easinessFactor;
        this.lastReviewedAt = lastReviewedAt;
        this.nextReviewAt = nextReviewAt;
    }

    // Getters
    public int getId() { return id; }
    public int getDeckId() { return deckId; }
    public String getFront() { return front; }
    public String getBack() { return back; }
    public String getMediaType() { return mediaType; }
    public String getDifficulty() { return difficulty; }
    public String getCreatedAt() { return createdAt; }
    public String getImagePath() { return imagePath; }
    public int getRepetitions() { return repetitions; }
    public double getEasinessFactor() { return easinessFactor; }
    public String getLastReviewedAt() { return lastReviewedAt; }
    public String getNextReviewAt() { return nextReviewAt; }

    public void setDifficulty(String difficulty) {
        //this.difficulty = difficulty;
    }

    // Setters for spaced repitition tracking
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setEasinessFactor(double easinessFactor) { this.easinessFactor = easinessFactor; }
    public void setLastReviewedAt(String lastReviewedAt) { this.lastReviewedAt = lastReviewedAt; }
    public void setNextReviewAt(String nextReviewAt) { this.nextReviewAt = nextReviewAt; }

    /**
     * Returns a string representation of the flashcard including its difficulty and image reference.
     *
     * @return a formatted string summarizing the flashcard
     */
    @Override
    public String toString()
    {
        return "[" + id + "] " + front + " → " + back + " (" + difficulty + ", image: " + imagePath + ")";
    }
}
