package utilities.controllers;

import db.DAO.FlashcardDAO;
import db.Flashcard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class ReviewFlashcardController {

    @FXML
    private Button showanswerbutton;

    private Flashcard flashcard;

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        System.out.println("Flashcard received: " + flashcard);
    }

    @FXML
    private void handleEasyClick(ActionEvent event) {
        updateDifficulty("Easy");
    }

    @FXML
    private void handleMediumClick(ActionEvent event) {
        updateDifficulty("Medium");
    }

    @FXML
    private void handleHardClick(ActionEvent event) {
        updateDifficulty("Hard");
    }

    private void updateDifficulty(String difficulty) {
        System.out.println("Flashcard when clicking: " + flashcard);
        if (flashcard == null) {
            System.out.println("No flashcard selected.");
            return;
        }

        try {
            flashcard.setDifficulty(difficulty); // Update in-memory
            FlashcardDAO.updateFlashcardDifficulty(flashcard.getId(), difficulty); // Persist to DB
            System.out.println("Difficulty updated to: " + difficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowAnswerButton(ActionEvent event) {
        try {
            // Load the second FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_2.fxml"));
            Parent root = loader.load();
            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcard(flashcard);

            // Get current stage from event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // You can replace this with custom error handling
        }
    }
}


/*package utilities.controllers;

import db.Flashcard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import db.DAO.FlashcardDAO;

import java.io.IOException;

public class ReviewFlashcardController {

    @FXML
    private Button showanswerbutton;
    private int currentFlashcardId = 1; // You need to set this dynamically as you review cards
    private Flashcard flashcard;

    @FXML
    private void handleEasyClick(ActionEvent event) {
        updateDifficulty("Easy");
    }

    @FXML
    private void handleMediumClick(ActionEvent event) {
        updateDifficulty("Medium");
    }

    @FXML
    private void handleHardClick(ActionEvent event) {
        updateDifficulty("Hard");
    }

    private void updateDifficulty(String difficulty) {
        if (flashcard == null) {
            System.out.println("No flashcard set.");
            return;
        }
        boolean success = FlashcardDAO.updateDifficulty(flashcard.getId(), difficulty);
        if (!success) {
            System.out.println("Failed to update difficulty for ID: " + flashcard.getId());
        }
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        System.out.println("Flashcard received in controller: " + flashcard);
    }

    @FXML
    void handleShowAnswerButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_2.fxml"));
            Parent root = loader.load();

            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcard(this.flashcard);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
