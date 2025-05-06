package utilities.controllers;

import utilities.services.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmField;

    @FXML
    private TextField nameField;

    @FXML
    private void handleSignUp(ActionEvent event) {
        String email = emailField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmField.getText();

        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.err.println("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.err.println("Passwords do not match");
            return;
        }
        if (AuthService.register(name, email, password)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Account already exists");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
