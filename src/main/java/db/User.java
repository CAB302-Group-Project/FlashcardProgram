package db;

/**
 * Model class representing a user of the flashcard system.
 * Stores identifying and contact information for authentication and personalization.
 */
public class User {
    private final int id;
    private String email;
    private String name;
    private String passwordHash;

    /**
     * Constructs a User object with full details.
     *
     * @param id           the unique ID of the user
     * @param name         the user's full name
     * @param email        the user's email address
     * @param passwordHash the user's hashed password (for internal use only)
     */
    public User(int id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Alternative constructor if you don't need password
    public User(int id, String name, String email) {
        this(id, name, email, null);
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
    public String getPasswordHash() {
        return passwordHash;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
