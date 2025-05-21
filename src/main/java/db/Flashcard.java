package db;

public class Flashcard
{
    private int id;
    private int deckId;
    private String front;
    private String back;
    private String mediaType;
    private String difficulty;
    private String createdAt;
    private String imagePath;

    public Flashcard(int id, int deckId, String front, String back, String mediaType, String difficulty, String createdAt, String imagePath)
    {
        this.id = id;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
        this.mediaType = mediaType;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
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

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString()
    {
        return "[" + id + "] " + front + " → " + back + " (" + difficulty + ", image: " + imagePath + ")";
    }
}
