package db;

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

    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setEasinessFactor(double easinessFactor) { this.easinessFactor = easinessFactor; }
    public void setLastReviewedAt(String lastReviewedAt) { this.lastReviewedAt = lastReviewedAt; }
    public void setNextReviewAt(String nextReviewAt) { this.nextReviewAt = nextReviewAt; }

    @Override
    public String toString()
    {
        return "[" + id + "] " + front + " → " + back + " (" + difficulty + ", image: " + imagePath + ")";
    }
}
