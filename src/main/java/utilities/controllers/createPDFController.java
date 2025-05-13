package utilities.controllers;

import ai.FlashcardResult;
import ai.prompt;
import app.FlashcardApp;
import ai.pdfReader;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class createPDFController {

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


    @FXML
    private void handleFile(ActionEvent event) throws IOException {

        String document = pdfReader.pdfExtract(); // Opening a file and getting the text from it
        // YOU CAN SAVE THIS DOCUMENT STRING IF YOU WANT UNDER THE DECK---------------------

        FlashcardResult deck = prompt.flashcardPrompt(document); // SAVE THIS TO DB UNDER THE DECK---------------

        List<String> deckQuestions = deck.questions; // I think this might be easier for you, so this is the Qs
        List<String> deckAnswers = deck.answers; // This is the A's

        if(deck != null) {
            System.out.println("Prompt successfully created.");
        }

        // Go back to create deck
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
}
