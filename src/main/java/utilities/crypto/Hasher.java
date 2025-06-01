package utilities.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hasher {
    /**
     * Hashes a password using BCrypt.
     * @param password the password to hash
     * @return the hashed password
     */
    public static String hash(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Verifies a password against a hashed password.
     * @param password the plain text password
     * @param hash the hashed password
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verify(String password, String hash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hash);
    }
}
