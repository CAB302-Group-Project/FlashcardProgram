package com.example.flashcardai.session;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.flashcardai.models.User;
import db.DBConnector;

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
            return !jwt.getExpiresAt().before(Date.from(instant));
        } catch (JWTVerificationException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public void logout() {
        this.user = null;
        this.token = "";
    }
}
