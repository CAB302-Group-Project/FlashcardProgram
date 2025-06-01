package utilities.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import db.User;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.services.UserSession;

import java.io.IOException;
import db.DAO.UserDAO;

/**
 * JavaFX controller for the Account screen.
 * Manages display and editing of user account information like name and email.
 * Includes navigation to the security dashboard.
 */
public class AccountController {
    @FXML
    private Text nameText;

    @FXML
    private Text emailText;

    @FXML
    private Text nameTextsml;

    @FXML
    private Text emailTextsml;


    /**
     * Initializes the UI and loads current user data once the platform is ready.
     */
    @FXML
    public void initialize() {
        Platform.runLater(this::updateUI);
    }

    /**
     * Populates name and email labels with the current user's data.
     */
    private void updateUI() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null && nameText != null && emailText != null && nameTextsml != null &emailTextsml != null) {
            nameText.setText(currentUser.getName());
            nameTextsml.setText(currentUser.getName());
            emailText.setText(currentUser.getEmail());
            emailTextsml.setText(currentUser.getEmail());
        } else {
            System.out.println("User or text fields not available");
        }
    }

    /**
     * Handles transition to the security dashboard scene.
     *
     * @param event the originating action event
     */
    @FXML
    private void securityDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Security.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Security Dashboard");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Opens a popup to edit the user's name.
     */
    @FXML
    private void handleEditName() {
        openEditPopup("Username", nameText.getText(), newValue -> {
            nameText.setText(newValue);
            nameTextsml.setText(newValue);
            persistNameToDatabase(newValue);
        });
    }

    /**
     * Opens a popup to edit the user's email.
     * Performs validation and duplicate checks.
     */
    @FXML
    private void handleEditEmail() {
        openEditPopup("Email", emailText.getText(), newValue -> {
            if (!isValidEmail(newValue)) {
                showAlert("Invalid Email", "Please enter a valid email address.");
                return;
            }

            User currentUser = UserSession.getInstance().getCurrentUser();
            if (!newValue.equalsIgnoreCase(currentUser.getEmail()) && UserDAO.emailExists(newValue)) {
                showAlert("Email Taken", "This email is already in use. Please choose another.");
                return;
            }

            if (UserDAO.emailExists(newValue)) {
                showAlert("Email Taken", "This email is already in use. Please choose another.");
                return;
            }

            emailText.setText(newValue);
            emailTextsml.setText(newValue);
            persistEmailToDatabase(newValue);
        });
    }

    /**
     * Validates the format of an email address.
     *
     * @param email the email string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Shows a warning alert with the given title and content.
     *
     * @param title   the title of the alert
     * @param content the body content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Opens the edit popup window for a specific field.
     *
     * @param field        the name of the field (e.g. Email or Username)
     * @param currentValue the current value to pre-fill
     * @param callback     the callback to handle submitted value
     */
    private void openEditPopup(String field, String currentValue, EditFieldPopupController.Callback callback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditFieldPopup.fxml"));
            Parent root = loader.load();

            EditFieldPopupController controller = loader.getController();
            controller.init(field, currentValue, callback);

            Stage stage = new Stage();
            stage.setTitle("Edit " + field);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the name in both the database and in-memory session.
     *
     * @param newName the new name to save
     */
    private void persistNameToDatabase(String newName) {
        User user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            user.setName(newName); // Update in-memory user
            UserDAO.updateName(user.getId(), newName);

            User updatedUser = UserDAO.getUserById(user.getId());
            UserSession.getInstance().setCurrentUser(updatedUser);
        }
    }

    /**
     * Updates the email in both the database and in-memory session.
     *
     * @param newEmail the new email to save
     */
    private void persistEmailToDatabase(String newEmail) {
        User user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            user.setEmail(newEmail); // Update in-memory user
            UserDAO.updateEmail(user.getId(), newEmail);

            User updatedUser = UserDAO.getUserById(user.getId());
            UserSession.getInstance().setCurrentUser(updatedUser);
        }
    }


}
