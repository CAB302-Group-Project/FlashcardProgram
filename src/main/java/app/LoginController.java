package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private void handleLogin() {
        System.out.println("Attempting login for: " + emailField.getText());
        // Later: Add authentication logic here
    }

    @FXML
    private void handleSignUp() {
        System.out.println("Redirecting to Sign Up screen...");
        // Later: Add navigation logic here
    }
}
