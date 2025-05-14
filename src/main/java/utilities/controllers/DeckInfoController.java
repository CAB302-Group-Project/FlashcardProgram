package utilities.controllers;

import db.DAO.DeckDAO;
import db.DAO.FlashcardDAO;
import db.Deck;
import db.User;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import app.FlashcardApp;
import javafx.util.Duration;

public class DeckInfoController {
    @FXML private TextField Prompt_Deck_Disc;
    @FXML private TextField Prompt_Deck_Title;
    @FXML private Label errorLabel;

    @FXML private void handleSave() {
        String title = Prompt_Deck_Title.getText();
        String description = Prompt_Deck_Disc.getText();
        User currentUser = FlashcardApp.getInstance().getCurrentUser();

        if (title == null || title.trim().isEmpty()) {
            errorLabel.setText("Deck title cannot be empty.");
            errorLabel.setVisible(true);

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> errorLabel.setVisible(false));
            delay.play();
            return;
        }

        if (currentUser == null || Deck.DeckDataHolder.questions == null) {
            errorLabel.setText("Unexpected error. Please restart.");
            errorLabel.setVisible(true);
            return;
        }

        int deckId = DeckDAO.insertDeck(currentUser.getId(), title, description);
        for (int i = 0; i < Deck.DeckDataHolder.questions.size(); i++) {
            FlashcardDAO.insertFlashcard(deckId,
                    Deck.DeckDataHolder.questions.get(i),
                    Deck.DeckDataHolder.answers.get(i));
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) Prompt_Deck_Title.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
