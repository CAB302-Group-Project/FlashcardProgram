package utilities.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utilities.crypto.Hasher;
import db.User;
import db.DAO.UserDAO;

import java.time.Instant;
import java.time.ZonedDateTime;

public class AuthService {
    /**
     * Registers a new user with the provided name, email, and password.
     * If the user already exists, registration fails.
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return true if registration is successful, false if the user already exists
     */
    public static boolean register(String name, String email, String password) {
        if (UserDAO.getUser(email) == null) {
            return UserDAO.insertUser(name, email, password); // plain password only
        }
        return false;
    }

    public static Algorithm algo = Algorithm.HMAC256("secret");

    /**
     * Authenticates a user with the provided username and password.
     * If authentication is successful, a JWT token is generated.
     * @param username the username of the user
     * @param password the password of the user
     * @return a JWT token if authentication is successful, null otherwise
     */
    public static String login(String username, String password) {
        User user = UserDAO.authUser(username, password);
        if (user != null) {
            try {
                // TODO: Update session timeout (+5 seconds)
                ZonedDateTime expireTime = ZonedDateTime.now().plusSeconds(5);
                Instant instant = expireTime.toInstant();

                return JWT.create()
                        .withIssuer("FlashcardProgram")
                        .withKeyId(user.getEmail())
                        .withExpiresAt(instant)
                        .sign(algo);
            } catch (JWTCreationException exception) {
                System.err.println(exception.getMessage());
            }
        }

        return null;
    }

    public static boolean changePassword(User user, String oldPassword, String newPassword) {
        User verified = UserDAO.authUser(user.getEmail(), oldPassword);
        if (verified != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedNewPassword = encoder.encode(newPassword);
            return UserDAO.updateUserPassword(user.getId(), hashedNewPassword);
        }
        return false;
    }
}
