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

/**
 * Controller for managing flashcard review sessions in a JavaFX application.
 * <p>
 * Handles the presentation flow of flashcards, difficulty rating input,
 * and spaced repetition scheduling. Manages two views:
 * <ol>
 *   <li>Question view (Review_Flashcards_1.fxml)</li>
 *   <li>Answer view (Review_Flashcards_2.fxml)</li>
 * </ol>
 * </p>
 *
 * <p><b>State Management:</b> Uses shared static fields to maintain state
 * between view transitions.</p>
 *
 * @author Della Rebu
 * @version 1.0
 * @see Flashcard
 * @see SpacedRepetitionScheduler
 * @since 1.0
 */
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

    /**
     * Initializes the flashcard review session with a list of flashcards.
     *
     * @param flashcards the list of flashcards to review (cannot be null)
     * @throws IllegalArgumentException if flashcards list is null
     */
    public void setFlashcards(List<Flashcard> flashcards) {
        sharedFlashcards = new ArrayList<>(flashcards);
        this.flashcards = sharedFlashcards;
        sharedCurrentIndex = 0;
        currentIndex = sharedCurrentIndex;

        if (!flashcards.isEmpty()) {
            currentFlashcard = flashcards.get(0);
            displayQuestion();
        }
    }

    /**
     * Sets the current flashcard and updates the view.
     *
     * @param flashcard the flashcard to display (cannot be null)
     */
    public void setFlashcard(Flashcard flashcard) {
        this.currentFlashcard = flashcard;
        if (questionlabel != null) {
            questionlabel.setText(flashcard.getFront());
        }
        if (answerlabel != null) {
            answerlabel.setText(flashcard.getBack());
        }
    }

    /**
     * Displays the current flashcard's question (front side).
     */
    private void displayQuestion() {
        if (currentFlashcard != null && questionlabel != null) {
            questionlabel.setText(currentFlashcard.getFront());
        }
    }

    /**
     * Displays the current flashcard's answer (back side).
     */
    private void displayAnswer() {
        if (currentFlashcard != null && answerlabel != null) {
            answerlabel.setText(currentFlashcard.getBack());
        }
    }

    /**
     * Handles "Easy" "Medium" "Hard" difficulty rating click.
     *
     * @param event the ActionEvent from the button click
     */
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

    /**
     * Disables all difficulty rating buttons.
     */
    private void disableDifficultyButtons() {
        if (easyButton != null) easyButton.setDisable(true);
        if (mediumButton != null) mediumButton.setDisable(true);
        if (hardButton != null) hardButton.setDisable(true);
    }

    /**
     * Enables all difficulty rating buttons.
     */
    private void enableDifficultyButtons() {
        if (easyButton != null) easyButton.setDisable(false);
        if (mediumButton != null) mediumButton.setDisable(false);
        if (hardButton != null) hardButton.setDisable(false);
    }

    /**
     * Updates the difficulty rating of the current flashcard and persists changes to the database.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Updates the difficulty in the current Flashcard object</li>
     *   <li>Persists the difficulty change to the database via {@link FlashcardDAO#updateFlashcardDifficulty(int, String)}</li>
     *   <li>Applies spaced repetition algorithm using {@link SpacedRepetitionScheduler#scheduleNextReview(Flashcard, String)}</li>
     *   <li>Persists the spaced repetition data (repetitions, easiness factor, review dates)
     *       via {@link FlashcardDAO#updateSpacedRepetitionData(int, int, double, String, String)}</li>
     * </ol>
     * </p>
     *
     * <p><b>Database Operations:</b> This method makes two separate database updates:
     * <ul>
     *   <li>Direct difficulty update</li>
     *   <li>Spaced repetition parameters update</li>
     * </ul>
     * Both operations are executed in a single transaction context.
     * </p>
     *
     * @param difficulty the new difficulty rating (case-sensitive, must be "Easy", "Medium", or "Hard")
     * @throws NullPointerException if currentFlashcard is null
     * @throws IllegalStateException if not connected to database
     * @see FlashcardDAO
     * @see SpacedRepetitionScheduler
     */
    private void updateDifficulty(String difficulty) {
        if (currentFlashcard == null) {
            System.out.println("No flashcard selected.");
            return;
        }

        try {
            currentFlashcard.setDifficulty(difficulty);
            FlashcardDAO.updateFlashcardDifficulty(currentFlashcard.getId(), difficulty);
            //System.out.println("Difficulty updated to: " + difficulty);
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

    /**
     * Advances to the next flashcard or returns to deck view when complete.
     *
     * @param event the ActionEvent that triggered this navigation
     */
    private void goToNextFlashcard(ActionEvent event) {
        if (flashcards == null || flashcards.isEmpty()) return;
        enableDifficultyButtons();

        // Move to next flashcard
        currentIndex++;
        debugState();

        if (currentIndex >= flashcards.size()) {
            // All flashcards reviewed
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Deck_Manager.fxml"));
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

    /**
     * Handles the "Show Answer" button click.
     *
     * @param event the ActionEvent from the button click
     */
    @FXML
    void handleShowAnswerButton(ActionEvent event) {
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

    /**
     * Sets the current flashcard index and updates the view.
     *
     * @param index the 0-based index of the flashcard to display
     */
    public void setCurrentIndex(int index) {
        sharedCurrentIndex = index;
        currentIndex = sharedCurrentIndex;
        if (sharedFlashcards != null && currentIndex < sharedFlashcards.size()) {
            currentFlashcard = sharedFlashcards.get(currentIndex);
            displayQuestion();
        }
    }

    /**
     * Prints debug information about the current controller state.
     * <p>
     * Includes:
     * <ul>
     *   <li>Current and shared indices</li>
     *   <li>Current flashcard details</li>
     *   <li>Sample of flashcards in the list</li>
     * </ul>
     * </p>
     */
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
        } else {
            System.out.println("No current flashcard!");
        }
        System.out.println("-------------------");
    }
}