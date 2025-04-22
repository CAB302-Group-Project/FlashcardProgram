package flashcard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Button btnSignup;

    @FXML
    private Button btnHome;

    @FXML
    private TextField textEmail;

    @FXML
    private PasswordField textPassword;

    @FXML
    private PasswordField textConfirmPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignup.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                App app = App.getAppInstance();
                String email = textEmail.getText();
                String password = textPassword.getText();
                String confirmPassword = textConfirmPassword.getText();
                if (password.equals(confirmPassword)) {
                    if (app.getSession().signup(email, password)) {
                        app.navigate("login");
                    }
                } else {
                    App.getAppInstance().alert("Passwords do not match");
                }
            }
        });

        btnHome.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                App.getAppInstance().navigate("main");
            }
        });
    }
}
