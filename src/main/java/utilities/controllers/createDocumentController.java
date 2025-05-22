package utilities.controllers;

import ai.*;
import app.FlashcardApp;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ai.prompt.flashcardTitle;

public class createDocumentController {

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
        // YOU CAN SAVE THIS DOCUMENT STRING IF YOU WANT UNDER THE DECK------------------

        FlashcardResult deck = prompt.flashcardPrompt(document); // The questions


        List<String> deckQuestions = deck.questions; // Questions
        System.out.println(deckQuestions);
        List<String> deckAnswers = deck.answers; // Answers
        System.out.println(deckAnswers);

        String flashcardTitle = prompt.flashcardTitle(deckQuestions); // String for the title
        String flashcardDesc = prompt.flashcardDesc(deckQuestions); // string for the description

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
