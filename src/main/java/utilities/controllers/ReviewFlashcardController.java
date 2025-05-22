package utilities.controllers;

import db.DAO.FlashcardDAO;
import db.Flashcard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import utilities.utils.SpacedRepetitionScheduler;
public class ReviewFlashcardController {

    @FXML
    void handleQuestionButton(ActionEvent event) {
        goToNextFlashcard(event);
    }

    @FXML
    private Button showanswerbutton;
    @FXML
    private Label questionlabel; // For question view
    @FXML
    private Label answerlabel;  // For answer view
    @FXML
    private Button easyButton;
    @FXML
    private Button mediumButton;
    @FXML
    private Button hardButton;

    private Flashcard currentFlashcard;
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private static List<Flashcard> sharedFlashcards;
    private static int sharedCurrentIndex;

    public void setFlashcards(List<Flashcard> flashcards) {
        sharedFlashcards = new ArrayList<>(flashcards);
        this.flashcards = sharedFlashcards;
        sharedCurrentIndex = 0;
        currentIndex = sharedCurrentIndex;

        System.out.println("\n==== Initializing Flashcards ====");
        debugState();  // <-- Add here to see initial state

        if (!flashcards.isEmpty()) {
            currentFlashcard = flashcards.get(0);
            displayQuestion();
        }
    }


    public void setFlashcard(Flashcard flashcard) {
        this.currentFlashcard = flashcard;
        if (questionlabel != null) {
            questionlabel.setText(flashcard.getFront());
        }
        if (answerlabel != null) {
            answerlabel.setText(flashcard.getBack());
        }
    }

    private void displayQuestion() {
        if (currentFlashcard != null && questionlabel != null) {
            questionlabel.setText(currentFlashcard.getFront());
        }
    }

    private void displayAnswer() {
        if (currentFlashcard != null && answerlabel != null) {
            answerlabel.setText(currentFlashcard.getBack());
        }
    }

    @FXML
    private void handleEasyClick(ActionEvent event) {
        updateDifficulty("Easy");
        disableDifficultyButtons();
    }

    @FXML
    private void handleMediumClick(ActionEvent event) {
        updateDifficulty("Medium");
        disableDifficultyButtons();
    }

    @FXML
    private void handleHardClick(ActionEvent event) {
        updateDifficulty("Hard");
        disableDifficultyButtons();
    }

    private void disableDifficultyButtons() {
        if (easyButton != null) easyButton.setDisable(true);
        if (mediumButton != null) mediumButton.setDisable(true);
        if (hardButton != null) hardButton.setDisable(true);
    }

    private void enableDifficultyButtons() {
        if (easyButton != null) easyButton.setDisable(false);
        if (mediumButton != null) mediumButton.setDisable(false);
        if (hardButton != null) hardButton.setDisable(false);
    }

    private void updateDifficulty(String difficulty) {
        if (currentFlashcard == null) {
            System.out.println("No flashcard selected.");
            return;
        }

        try {
            currentFlashcard.setDifficulty(difficulty);
            FlashcardDAO.updateFlashcardDifficulty(currentFlashcard.getId(), difficulty);
            System.out.println("Difficulty updated to: " + difficulty);
            // Apply spaced repetition algorithm
            SpacedRepetitionScheduler.scheduleNextReview(currentFlashcard, difficulty);

            // Save spaced repetition data
            FlashcardDAO.updateSpacedRepetitionData(
                    currentFlashcard.getId(),
                    currentFlashcard.getRepetitions(),
                    currentFlashcard.getEasinessFactor(),
                    currentFlashcard.getLastReviewedAt(),
                    currentFlashcard.getNextReviewAt()
            );

            System.out.println("Difficulty updated to: " + difficulty);
            System.out.println("Next review scheduled for: " + currentFlashcard.getNextReviewAt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToNextFlashcard(ActionEvent event) {
        System.out.println("\n==== Before Next Flashcard ====");
        if (flashcards == null || flashcards.isEmpty()) return;
        enableDifficultyButtons();

        // Move to next flashcard
        currentIndex++;
        System.out.println("\n==== After Incrementing Index ====");
        debugState();  // <-- Add here to verify index increment

        if (currentIndex >= flashcards.size()) {
            // All flashcards reviewed
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DeckView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Load question view for next flashcard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_1.fxml"));
            Parent root = loader.load();
            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcards(flashcards);
            controller.setCurrentIndex(currentIndex);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowAnswerButton(ActionEvent event) {
        System.out.println("\n==== Showing Answer ====");
        debugState();  // <-- Add here to see state when showing answer
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_2.fxml"));
            Parent root = loader.load();
            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcards(flashcards);
            controller.setCurrentIndex(currentIndex);
            controller.displayAnswer();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        if (flashcards != null && currentIndex < flashcards.size()) {
            currentFlashcard = flashcards.get(currentIndex);
        }
    }*/
    public void setCurrentIndex(int index) {
        sharedCurrentIndex = index;
        currentIndex = sharedCurrentIndex;
        System.out.println("\n==== Setting Current Index ====");
        debugState();  // <-- Add here to verify index setting
        if (sharedFlashcards != null && currentIndex < sharedFlashcards.size()) {
            currentFlashcard = sharedFlashcards.get(currentIndex);
            displayQuestion();
        }
    }

    private void debugState() {
        System.out.println("--- Current State ---");
        System.out.println("Current Index: " + currentIndex +
                " (Shared: " + sharedCurrentIndex + ")");
        System.out.println("Total Cards: " + (sharedFlashcards != null ? sharedFlashcards.size() : 0));

        if (currentFlashcard != null) {
            System.out.println("Current Flashcard:");
            System.out.println("  ID: " + currentFlashcard.getId());
            System.out.println("  Question: " + currentFlashcard.getFront());
            System.out.println("  Answer: " + currentFlashcard.getBack());
            //System.out.println("  Difficulty: " + currentFlashcard.getDifficulty());
        } else {
            System.out.println("No current flashcard!");
        }

        if (sharedFlashcards != null && !sharedFlashcards.isEmpty()) {
            System.out.println("First 3 flashcards:");
            int showCount = Math.min(3, sharedFlashcards.size());
            for (int i = 0; i < showCount; i++) {
                Flashcard f = sharedFlashcards.get(i);
                System.out.println("  [" + i + "] " + f.getFront() + " / " + f.getBack());
            }
        }
        System.out.println("-------------------");
    }
}