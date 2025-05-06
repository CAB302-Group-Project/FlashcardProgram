package utilities.controllers;

import app.FlashcardApp;
import utilities.models.Deck;
import utilities.services.ApiService;
import db.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.List;

public class DashboardController {
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        updateUI();
    }

    public void initialize() {
        currentUser = FlashcardApp.getInstance().getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
        else
        {
            // Redirect to log in
        }
    }

    private final ApiService apiService;

    public DashboardController() {
        this.apiService = new ApiService();
    }

    public void displayDecks() {
        List<Deck> decks = apiService.fetchDecks();  // Fix this typo (Deck instead of Deck)
        System.out.println("Available Flashcard Decks:");
        decks.forEach(deck -> System.out.println("- " + deck.getName() + ": " + deck.getDescription()));
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
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
    private Text nameText;

    private void updateUI() {
        if (currentUser != null && nameText != null) {
            nameText.setText(currentUser.getName());
        }
    }

}