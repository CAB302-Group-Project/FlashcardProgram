package utilities.controllers;

import ai.FlashcardResult;
import ai.prompt;
import app.FlashcardApp;
import ai.pdfReader;
import db.Deck;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

/**
 * A class to handle the create PDF page in the app, fit with a back button and an open file button for deck creation.
 */

public class createPDFController {

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
     * handles the execution of the open file button. Opens a file explorer to choose a pdf.
     * After which it creates a deck.
     * @param event event activation upon button press.
     * @throws IOException Checks for mishaps trying to go back to the create deck screen.
     */

    @FXML
    private void handleFile(ActionEvent event) throws IOException {

        String document = pdfReader.pdfExtract(); // Opening a file and getting the text from it
        // YOU CAN SAVE THIS DOCUMENT STRING IF YOU WANT UNDER THE DECK---------------------

        FlashcardResult deck = prompt.flashcardPrompt(document); // SAVE THIS TO DB UNDER THE DECK---------------

        List<String> deckQuestions = deck.questions; // I think this might be easier for you, so this is the Qs
        List<String> deckAnswers = deck.answers; // This is the A's

        if(deck != null) {
            System.out.println("Prompt successfully created.");
            Deck.DeckDataHolder.questions = deck.questions;
            Deck.DeckDataHolder.answers = deck.answers;
        }

        // Go back to create deck
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("fxml/Set_Deck_Information.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Set_Deck_Information");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}