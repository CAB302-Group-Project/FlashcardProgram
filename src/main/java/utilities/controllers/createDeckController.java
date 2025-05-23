package utilities.controllers;

import app.FlashcardApp;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * A class to handle the create deck page in the app, fit with a back button and links to create different deck types.
 */

public class createDeckController {

    /**
     * handles the execution of the back button. Sends back to the dashboard.
     * @param event event activation upon button press.
     */

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handles the execution of the keywords button. Leads to a page where keywords can be input to create AI decks.
     * @param event event activation upon button press.
     */

    @FXML
    private void handleKeywords(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck_Keywords.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Deck - Keywords");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handles the execution of the Document button. Leads to a page where files can be uploaded to create AI decks.
     * @param event event activation upon button press.
     */

    @FXML
    private void handleDocument(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck_Document.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Deck - Document");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handles the execution of the PDF button. Leads to a page where files can be uploaded to create AI decks.
     * @param event event activation upon button press.
     */

    @FXML
    private void handlePDF(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck_PDF.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Deck - PDF");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}