package com.example.flashcardai.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.flashcardai.crypto.Hasher;
import db.User;
import db.UserDAO;

import java.time.Instant;
import java.time.ZonedDateTime;

public class AuthService {
    public static boolean register(String username, String password) {
        db.User user = UserDAO.getUser(username);
        if (user == null) {
            String hashed = Hasher.hash(password);
            UserDAO.insertUser(username, hashed);
            return true;
        }

        return false;
    }

    public static Algorithm algo = Algorithm.HMAC256("secret");

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
}
