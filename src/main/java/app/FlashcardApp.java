package app;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.flashcardai.models.User;
import com.example.flashcardai.services.AuthService;
import db.DBConnector;
import db.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage; // <-- also make sure you import this!

import java.sql.Connection;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

public class FlashcardApp extends Application {
    private static FlashcardApp appInstance;

    private String sessionToken;

    private Connection dbInstance;

    @Override
    public void init() {
        appInstance = this;
    }

    @Override
    public void start(Stage primaryStage) {
        dbInstance = DBConnector.connect();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));

            Scene scene = new Scene(root, 800, 600); // width x height

            primaryStage.setTitle("Flashcard App");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

    public static FlashcardApp getInstance() {
        return appInstance;
    }

    public Connection getDBConnection() {
        return this.dbInstance;
    }

    public void setSessionToken(String token) {
        this.sessionToken = token;
    }

    public User getSession() {
        if (sessionToken != null) {
            try {
                JWTVerifier verifier = JWT.require(AuthService.algo)
                        .withIssuer("FlashcardProgram")
                        .build();

                DecodedJWT jwt = verifier.verify(sessionToken);
                ZonedDateTime currentTime = ZonedDateTime.now();
                Instant instant = currentTime.toInstant();
                if (jwt.getExpiresAt().before(Date.from(instant))) {
                    return null;
                }

                String email = jwt.getKeyId();
                return UserDAO.getUserIdByEmail(email);
            } catch (JWTVerificationException e) {
                // Session timed out. Redirect
                System.err.println(e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
}
