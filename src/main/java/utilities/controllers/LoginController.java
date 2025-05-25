package utilities.controllers;

import db.User;
import utilities.controllers.DashboardController;
import app.FlashcardApp;
import db.DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import utilities.services.UserSession;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    /**
     * Login user with email and password.
     * If successful, redirects to the dashboard.
     * If unsuccessful, displays an error message.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please fill out both email and password fields");
            return;
        }

        User user = UserDAO.authUser(email, password);  // Make sure this exists

        if (user != null) {
            try {
                FlashcardApp.getInstance().setUserId(user.getId());
                UserSession.getInstance().setCurrentUser(user);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                Parent root = loader.load();



                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load dashboard.");
            }
        } else {
            errorLabel.setText("Invalid email or password.");
        }
    }

    /**
     * Redirects to the sign-up page.
     */
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Sign_Up.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}