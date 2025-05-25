package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * JavaFX controller for the Deck Info popup dialog.
 * Allows user to input a deck title and description before deck creation.
 */
public class DeckInfoPopupController {

    /**
     * Callback interface to handle submission of deck info.
     */
    public interface Callback {
        void onDeckInfoEntered(String title, String description);
    }

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private javafx.scene.control.Button confirmButton;

    private Callback callback;


    /**
     * Initializes the popup and binds field input validation.
     *
     * @param callback the callback function to invoke on confirmation
     */
    public void init(Callback callback) {
        this.callback = callback;

        // Disable confirm button if title field is empty
        confirmButton.setDisable(true);

        titleField.textProperty().addListener((obs, oldText, newText) -> {
            confirmButton.setDisable(newText.trim().isEmpty());
        });
    }

    /**
     * Handles the confirm action, passing deck info to the callback and closing the popup.
     */
    @FXML
    private void handleConfirm() {
        if (callback != null) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            callback.onDeckInfoEntered(title.isEmpty() ? "Untitled Deck" : title,
                    description.isEmpty() ? "No description" : description);
        }

        // Close popup
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
