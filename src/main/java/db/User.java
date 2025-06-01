package db;

/**
 * Model class representing a user of the flashcard system.
 * Stores identifying and contact information for authentication and personalization.
 */
public class User {
    private final int id;
    private String email;
    private String name;

    /**
     * Constructs a User object.
     *
     * @param id    the unique ID of the user
     * @param name  the user's full name
     * @param email the user's email address
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
