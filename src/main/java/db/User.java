package db;

public class User {
    private final int id;
    private String email;
    private String name;

    /**
     * User class constructor
     * @param id user unique identification
     * @param name user name
     * @param email user email
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
