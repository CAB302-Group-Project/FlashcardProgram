package utilities.controllers;

import ai.FlashcardResult;
import ai.pdfReader;
import ai.prompt;
import app.FlashcardApp;
import db.Deck;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class createKeywordController {

    /**
     * handles the execution of the back button. Sends back to the dashboard.
     * @param event event activation upon button press.
     */

    @FXML
    private void handleBack(ActionEvent event) {
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

    /**
     * just a declaration to identify a text field.
     */

    @FXML
    private TextArea inputField;

    /**
     * Just a declaration to get the contents of the text field.
     */

    private String userInput;

    /**
     * handles the execution of the submit button. Takes the contents of the text area and submits it for deck creation.
     * @param event event activation upon button press.
     * @throws IOException Checks for mishaps trying to go back to the create deck screen.
     */

    @FXML
    private void handleSubmit(ActionEvent event) throws IOException {
        userInput = inputField.getText().trim();

        if (userInput.isEmpty()) {
            System.err.println("Input field is empty.");
            return;
        }

        System.out.println("User input: " + userInput);
        FlashcardResult deck = prompt.flashcardPrompt(userInput);

        if (deck == null || deck.questions == null || deck.answers == null) {
            System.err.println("Flashcard generation failed.");
            return;
        }

        openDeckInfoPopup((title, description) -> {
            int userId = utilities.services.UserSession.getInstance().getCurrentUser().getId();
            int deckId = db.DAO.DeckDAO.insertDeck(userId, title, description);

            if (deckId != -1) {
                for (int i = 0; i < deck.questions.size(); i++) {
                    String front = deck.questions.get(i);
                    String back = deck.answers.get(i);
                    db.DAO.FlashcardDAO.insertFlashcard(deckId, front, back, "text", "medium", null);
                }

                Deck.DeckDataHolder.questions = deck.questions;
                Deck.DeckDataHolder.answers = deck.answers;
            }

            // Return to create deck screen
            try {
                FlashcardApp.getInstance().setSessionToken(null);
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck.fxml"));
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Deck Created");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void openDeckInfoPopup(java.util.function.BiConsumer<String, String> callback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DeckInfoPopup.fxml"));
            Parent root = loader.load();

            DeckInfoPopupController controller = loader.getController();
            controller.init(callback::accept);

            Stage popupStage = new Stage();
            popupStage.setTitle("Deck Details");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
