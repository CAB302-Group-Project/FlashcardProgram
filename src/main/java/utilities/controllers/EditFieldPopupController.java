package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * JavaFX controller for a generic field editing popup window.
 * Supports dynamic field editing for items like username or email.
 */
public class EditFieldPopupController {

    @FXML
    private TextField inputField;

    @FXML
    private Label labelTitle;

    private String fieldType;
    private String currentValue;
    private Callback callback;

    /**
     * Callback interface to return the edited value to the calling context.
     */
    public interface Callback {
        void onSave(String newValue);
    }

    /**
     * Initializes the popup with a field label and current value.
     *
     * @param fieldType    the name of the field being edited (e.g., "Email")
     * @param currentValue the current value of the field
     * @param callback     the callback to handle the submitted value
     */
    public void init(String fieldType, String currentValue, Callback callback) {
        this.fieldType = fieldType;
        this.currentValue = currentValue;
        this.callback = callback;

        labelTitle.setText("Edit " + fieldType);
        inputField.setText(currentValue);
    }

    /**
     * Handles saving the input and triggering the callback.
     */
    @FXML
    private void handleSave() {
        if (callback != null) {
            callback.onSave(inputField.getText());
        }
        closeWindow();
    }

    /**
     * Handles canceling the edit and closing the popup.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }


    /**
     * Closes the popup window.
     */
    private void closeWindow() {
        Stage stage = (Stage) inputField.getScene().getWindow();
        stage.close();
    }
}
