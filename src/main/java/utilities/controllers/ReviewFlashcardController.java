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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

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

    /** Button to navigate to the next question or finish review */
    @FXML
    private Button showquestionbutton;

    /** Button to show the answer of the current flashcard */
    @FXML
    private Button showanswerbutton;

    /** Label displaying the current flashcard question */
    @FXML
    private Label questionlabel;

    /** Label displaying the current flashcard answer */
    @FXML
    private Label answerlabel;

    /** Button to rate the flashcard as easy */
    @FXML
    private Button easyButton;

    /** Button to rate the flashcard as medium */
    @FXML
    private Button mediumButton;

    /** Button to rate the flashcard as hard */
    @FXML
    private Button hardButton;

    /** Label showing the current position in the deck */
    @FXML
    private Label positionLabel;

    /** Label displaying next review information */
    @FXML
    private Label reviewLabel;

    /** Label shown when reaching the end of the deck */
    @FXML
    private Label endOfDeckLabel;

    /** Label displaying the current deck topic */
    @FXML
    private Label topicLabel;

    /** Shared deck name across controller instances */
    private static String sharedDeckName = "";

    /** Current flashcard being reviewed */
    private Flashcard currentFlashcard;

    /** List of flashcards in the current review session */
    private List<Flashcard> flashcards;

    /** Current index in the flashcards list */
    private int currentIndex = 0;

    /** Shared flashcards list across controller instances */
    private static List<Flashcard> sharedFlashcards;

    /** Shared current index across controller instances */
    private static int sharedCurrentIndex;

    /** Flag indicating if current card is the last in the deck */
    private boolean isLastCard = false;

    /**
     * Sets the deck name and updates the topic label.
     *
     * @param deckName the name of the current deck
     */
    public void setDeckName(String deckName) {
        this.sharedDeckName = deckName;
        if (topicLabel != null) {
            topicLabel.setText(deckName);
        }
    }

    /**
     * Handles the "Next Question" or "Finish Review" button action.
     *
     * @param event the ActionEvent triggered by the button
     */
    @FXML
    void handleQuestionButton(ActionEvent event) {
        goToNextFlashcard(event);
    }

    /**
     * Initializes the flashcard review session with a list of flashcards.
     *
     * @param flashcards the list of flashcards to review
     * @throws IllegalArgumentException if flashcards list is null
     */
    public void setFlashcards(List<Flashcard> flashcards) {
        sharedFlashcards = new ArrayList<>(flashcards);
        this.flashcards = sharedFlashcards;
        sharedCurrentIndex = 0;
        currentIndex = sharedCurrentIndex;

        if (reviewLabel != null) {
            reviewLabel.setText("");
            reviewLabel.setVisible(false);
        }

        if (endOfDeckLabel != null) {
            endOfDeckLabel.setVisible(false);
        }

        if (showanswerbutton != null) {
            showanswerbutton.setText("Show Answer");
        }

        if (!flashcards.isEmpty()) {
            currentFlashcard = flashcards.get(0);
            displayQuestion();
        }
    }

    /**
     * Sets the current flashcard and updates the view.
     *
     * @param flashcard the flashcard to display
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
            if (positionLabel != null && flashcards != null) {
                positionLabel.setText(String.format("Question %d of %d",
                        currentIndex + 1,
                        flashcards.size()));
            }
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
     * Handles "Easy" difficulty rating click.
     *
     * @param event the ActionEvent from the button click
     */
    @FXML
    private void handleEasyClick(ActionEvent event) {
        updateDifficulty("Easy");
        disableDifficultyButtons();
        reviewLabel.setVisible(true);
    }

    /**
     * Handles "Medium" difficulty rating click.
     *
     * @param event the ActionEvent from the button click
     */
    @FXML
    private void handleMediumClick(ActionEvent event) {
        updateDifficulty("Medium");
        disableDifficultyButtons();
        reviewLabel.setVisible(true);
    }

    /**
     * Handles "Hard" difficulty rating click.
     *
     * @param event the ActionEvent from the button click
     */
    @FXML
    private void handleHardClick(ActionEvent event) {
        updateDifficulty("Hard");
        disableDifficultyButtons();
        reviewLabel.setVisible(true);
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
     * Updates the difficulty rating of the current flashcard and persists changes.
     *
     * @param difficulty the new difficulty rating ("Easy", "Medium", or "Hard")
     */
    private void updateDifficulty(String difficulty) {
        if (currentFlashcard == null) {
            System.out.println("No flashcard selected.");
            return;
        }

        try {
            currentFlashcard.setDifficulty(difficulty);
            FlashcardDAO.updateFlashcardDifficulty(currentFlashcard.getId(), difficulty);
            SpacedRepetitionScheduler.scheduleNextReview(currentFlashcard, difficulty);

            FlashcardDAO.updateSpacedRepetitionData(
                    currentFlashcard.getId(),
                    currentFlashcard.getRepetitions(),
                    currentFlashcard.getEasinessFactor(),
                    currentFlashcard.getLastReviewedAt(),
                    currentFlashcard.getNextReviewAt()
            );

            if (reviewLabel != null) {
                String nextReviewText = currentFlashcard.getNextReviewAt() != null ?
                        "Next Review: " + formatDate(currentFlashcard.getNextReviewAt()) :
                        "Next Review: Not scheduled";
                reviewLabel.setText(nextReviewText);
            }

            System.out.println("Difficulty updated to: " + difficulty);
            System.out.println("Next review scheduled for: " + currentFlashcard.getNextReviewAt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Formats a date string from yyyy-MM-dd to dd-MM.
     *
     * @param dateStr the date string in yyyy-MM-dd format
     * @return formatted date string in dd-MM format, or original if parsing fails
     */
    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("dd-MM"));
        } catch (DateTimeParseException e) {
            return dateStr;
        }
    }

    /**
     * Sets whether the current card is the last in the deck.
     *
     * @param isLastCard true if current card is last, false otherwise
     */
    public void setIsLastCard(boolean isLastCard) {
        this.isLastCard = isLastCard;
        if (showquestionbutton != null && isLastCard) {
            showquestionbutton.setText("Finish Review");
        }
    }

    /**
     * Checks if current card is the last in the deck.
     *
     * @return true if current card is last, false otherwise
     */
    private boolean isLastCard() {
        return flashcards != null && currentIndex == flashcards.size() - 1;
    }

    /**
     * Advances to the next flashcard or returns to deck manager when complete.
     *
     * @param event the ActionEvent that triggered this navigation
     */
    private void goToNextFlashcard(ActionEvent event) {
        if (flashcards == null || flashcards.isEmpty()) {
            System.out.println("Flashcards list is null or empty");
            return;
        }
        enableDifficultyButtons();

        if (isLastCard && showquestionbutton != null &&
                "Finish Review".equals(showquestionbutton.getText())) {
            returnToDeckManager(event);
            return;
        }

        currentIndex++;
        sharedCurrentIndex = currentIndex;
        debugState();

        if (currentIndex >= flashcards.size()) {
            returnToDeckManager(event);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_1.fxml"));
            Parent root = loader.load();
            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcards(flashcards);
            controller.setCurrentIndex(currentIndex);
            controller.setDeckName(sharedDeckName);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns to the deck manager view.
     *
     * @param event the ActionEvent that triggered this navigation
     */
    private void returnToDeckManager(ActionEvent event) {
        System.out.println("\n=== Finished reviewing all flashcards ===");
        System.out.println("Returning to deck manager");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Deck_Manager.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBacktoDeckView(ActionEvent event) {
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
            controller.setDeckName(sharedDeckName);
            controller.displayAnswer();
            controller.setIsLastCard(currentIndex == flashcards.size() - 1);

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
            if (topicLabel != null && sharedDeckName != null) {
                topicLabel.setText(sharedDeckName);
            }
        }
    }

    /**
     * Prints debug information about the current controller state.
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