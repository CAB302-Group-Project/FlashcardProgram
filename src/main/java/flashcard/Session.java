package flashcard;

import java.sql.Connection;

import db.User;
import db.UserDAO;
import db.DBConnector;
import flashcard.Crypto.Hasher;

public class Session {
    private String token;

    private User user;

    private final Connection conn;

    public Session() {
        token = "";
        user = null;
        conn = DBConnector.connect();
        if (conn == null) {
            System.out.println("Unable to connect to database");
        }
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean login(String email, String password) {
        if (conn == null) return false;

        User user = UserDAO.authUser(email, password);
        if (user != null) {
            // TODO: Generate token using email and with expiration
            this.user = user;
            this.token = "<TOKEN>";
            return true;
        }

        App.getAppInstance().alert("Unable to authenticate user");
        return false;
    }

    public boolean signup(String email, String password) {
        if (conn == null) return false;

        User user = UserDAO.getUser(email);
        if (user == null) {
            String hashed = Hasher.hash(password);
            UserDAO.insertUser(email, hashed);
            return true;
        } else {
            App.getAppInstance().alert("Email already exists");
            return false;
        }
    }

    public void logout() {
        this.user = null;
        this.token = "";
    }
}
