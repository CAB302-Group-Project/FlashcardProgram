package utilities.controllers;

import app.FlashcardApp;
import app.PomodoroTimerController;
import db.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import utilities.services.UserSession;

import javax.swing.*;

/**
 * JavaFX controller for the main Dashboard.
 * Handles routing to various application features such as deck creation, quizzes,
 * pomodoro timer, account management, and progress tracking.
 */
public class DashboardController {
    @FXML
    private Text nameText;

    /**
     * Initializes the dashboard and displays the current user's name.
     */
    @FXML
    public void initialize() {
        Platform.runLater(this::updateUI);
    }

    /**
     * Updates the dashboard UI with the user's display name.
     */
    private void updateUI() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null && nameText != null) {
            nameText.setText(currentUser.getName());
        }
    }

    /**
     * Logs the user out and redirects to the login screen.
     *
     * @param event the triggering event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
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


    @FXML
    private void handleCreateDeck(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Deck");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePomodoro(ActionEvent event) {
        try {
            PomodoroTimerController controller = new PomodoroTimerController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Pomodoro_Timer.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pomodoro");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void handleDeckManager(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Deck_Manager.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Deck Manager");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Opens the Account Dashboard view.
     *
     * @param event the triggering event
     */
    @FXML
    private void accountDashboard(ActionEvent event) {

        try {
            System.out.println("From Dashboard → Current user: " + UserSession.getInstance().getCurrentUser());

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
    private void handleTrackProgress(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TrackProgress.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Track Progress");
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load TrackProgress.fxml: " + e.getMessage());
            e.printStackTrace(); // add this to see the full error trace
        }
    }

}