package db;

public class User {
    private final int id;
    private final String email;
    private final String name;

    /**
     * User class constructor
     * @param id user unique identification
     * @param email user email
     * @param name user name
     */
    public User(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    /**
     * Returns user id
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns user email
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns user name
     * @return user name
     */
    public String getName() {
        return name;
    }
}
