package db;

public class User {
    private final int id;
    private final String email;
    private final String name;

    public User(int id, String name, String email) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    /**
     *
     * @return
     */
    
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
