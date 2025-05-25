package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EditFieldPopupController {

    @FXML
    private TextField inputField;

    @FXML
    private Label labelTitle;

    private String fieldType;
    private String currentValue;
    private Callback callback;

    public interface Callback {
        void onSave(String newValue);
    }

    public void init(String fieldType, String currentValue, Callback callback) {
        this.fieldType = fieldType;
        this.currentValue = currentValue;
        this.callback = callback;

        labelTitle.setText("Edit " + fieldType);
        inputField.setText(currentValue);
    }

    @FXML
    private void handleSave() {
        if (callback != null) {
            callback.onSave(inputField.getText());
        }
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) inputField.getScene().getWindow();
        stage.close();
    }
}
