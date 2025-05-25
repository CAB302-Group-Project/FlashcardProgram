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

public class securityDashboardController {
    @FXML
    private Text nameText;

    @FXML
    private Text emailText;

    @FXML
    public void initialize() {
        Platform.runLater(this::updateUI);
    }

    private void updateUI() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null && nameText != null && emailText != null) {
            nameText.setText(currentUser.getName());
            emailText.setText(currentUser.getEmail());
        } else {
            System.out.println("User or text fields not available");
        }
    }

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

    @FXML
    private void changePasswordButton(ActionEvent event) {
        // TODO: Implement password change logic
        System.out.println("Change Password button clicked.");
    }

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
