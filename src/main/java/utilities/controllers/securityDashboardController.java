package utilities.controllers;

import app.FlashcardApp;
import db.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.services.UserSession;

/**
 * JavaFX controller for the Security Dashboard screen.
 * Provides access to security-related features such as login history,
 * password changes, and account settings.
 */
public class securityDashboardController {
    @FXML
    private Text nameText;

    @FXML
    private Text emailText;

    /**
     * Initializes the security dashboard and updates the user UI elements.
     */
    @FXML
    public void initialize() {
        Platform.runLater(this::updateUI);
    }

    /**
     * Populates the name and email text fields with current user information.
     */
    private void updateUI() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null && nameText != null && emailText != null) {
            nameText.setText(currentUser.getName());
            emailText.setText(currentUser.getEmail());
        } else {
            System.out.println("User or text fields not available");
        }
    }

    /**
     * Opens the login history screen.
     *
     * @param event the triggering event
     */
    @FXML
    private void loginHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login_History.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Security Dashboard");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Opens the account settings screen.
     *
     * @param event the triggering event
     */
    @FXML
    private void accountbutton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Account.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Account Dashboard");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Placeholder for password change functionality.
     *
     * @param event the triggering event
     */
    @FXML
    private void changePasswordButton(ActionEvent event) {
        // TODO: Implement password change logic
        System.out.println("Change Password button clicked.");
    }

    /**
     * Logs out the user and redirects to the login screen.
     *
     * @param event the triggering event
     */
    @FXML
    private void LogoutButton(ActionEvent event) {
        try {
            UserSession.getInstance().clearSession();
            FlashcardApp.getInstance().setUserId(-1);
            FlashcardApp.getInstance().setSessionToken(null);

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
