package utilities.controllers;

import db.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import utilities.services.AuthService;
import utilities.services.UserSession;

public class ChangePasswordController {
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;

    private void goToSecurityDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Security.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) oldPasswordField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Security Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ConfirmPasswordChange(ActionEvent event) {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Error, Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorLabel.setText("Error, New passwords do not match.");
            return;
        }

        if (newPassword.length() < 8) {
            errorLabel.setText("Error, Password must be at least 8 characters long.");
            return;
        }

        User user = UserSession.getInstance().getCurrentUser();
        if (user == null) {
            showAlert("Error", "No user in session.");
            return;
        }

        boolean success = AuthService.changePassword(user, oldPassword, newPassword);

        showAlert("Success", "Password changed successfully.");
        if (success) {
            showAlert("Success", "Password updated successfully.");
        } else {
            showAlert("Error", "Old password incorrect. Password not changed.");
        }

        showAlert("Success", "Password changed successfully.");
        goToSecurityDashboard();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
        }
}

