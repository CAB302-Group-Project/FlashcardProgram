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
        userInput = inputField.getText();
        System.out.println("User input: " + userInput);

        FlashcardResult deck = prompt.flashcardPrompt(userInput); // SAVE THIS TO DB UNDER THE DECK-------------

        List<String> deckQuestions = deck.questions; // Questions

        List<String> deckAnswers = deck.answers; // Answers


        String flashcardTitle = prompt.flashcardTitle(deckQuestions); // String for the title
        String flashcardDesc = prompt.flashcardDesc(deckQuestions); // string for the description

        if(deck != null) {
            System.out.println("Prompt successfully created.");
            Deck.DeckDataHolder.questions = deck.questions;
            Deck.DeckDataHolder.answers = deck.answers;
        }

        // Go back to create deck
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Deck Created!");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
