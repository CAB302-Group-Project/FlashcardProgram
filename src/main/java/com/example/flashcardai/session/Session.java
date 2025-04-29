package com.example.flashcardai.session;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import db.User;
import db.UserDAO;
import db.DBConnector;
import com.example.flashcardai.app.App;
import com.example.flashcardai.crypto.Hasher;

public class Session {
    private String token;

    private User user;

    private final Algorithm algo;

    private final Connection conn;

    public Session() {
        token = "";
        user = null;
        algo = Algorithm.HMAC256("SECRET_KEY");
        conn = DBConnector.connect();
        if (conn == null) {
            System.err.println("Unable to connect to database");
        }
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean checkToken () {
        // Regularly check token
        try {
            JWTVerifier verifier = JWT.require(algo)
                    .withIssuer("FlashcardProgram")
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            ZonedDateTime currentTime = ZonedDateTime.now();
            Instant instant = currentTime.toInstant();
            if (jwt.getExpiresAt().before(Date.from(instant))) {
                return false;
            }

            return true;
        } catch (JWTVerificationException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean login(String email, String password) {
        if (conn == null) return false;

        User user = UserDAO.authUser(email, password);
        if (user != null) {
            this.user = user;
            try {
                // TODO: Update session timeout (+5 seconds)
                ZonedDateTime expireTime = ZonedDateTime.now().plusSeconds(5);
                Instant instant = expireTime.toInstant();
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", user.getEmail());
                payload.put("id", user.getId());

                this.token = JWT.create()
                        .withIssuer("FlashcardProgram")
                        .withExpiresAt(instant)
                        .withPayload(payload)
                        .sign(algo);
                return true;
            } catch (JWTCreationException exception) {
                System.err.println(exception.getMessage());
            }
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
        App.getAppInstance().navigate("login");
    }
}
