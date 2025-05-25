package utilities.controllers;

import db.DBConnector;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import db.User;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.services.UserSession;

import java.io.IOException;
import db.DAO.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountController {
    @FXML
    private Text nameText;

    @FXML
    private Text emailText;

    @FXML
    private Text nameTextsml;

    @FXML
    private Text emailTextsml;

    @FXML
    public void initialize() {
        Platform.runLater(this::updateUI);
    }

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

    @FXML
    private void handleEditName() {
        openEditPopup("Username", nameText.getText(), newValue -> {
            nameText.setText(newValue);
            nameTextsml.setText(newValue);
            persistNameToDatabase(newValue);
        });
    }

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

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

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

    private void persistNameToDatabase(String newName) {
        User user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            user.setName(newName); // Update in-memory user
            UserDAO.updateName(user.getId(), newName);

            User updatedUser = UserDAO.getUserById(user.getId());
            UserSession.getInstance().setCurrentUser(updatedUser);
        }
    }

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
