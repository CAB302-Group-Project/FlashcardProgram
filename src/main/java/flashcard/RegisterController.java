package flashcard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import db.UserDAO;
import db.DBConnector;
import db.User;

import flashcard.Crypto.Hasher;

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

    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = DBConnector.connect();
        if (conn == null) {
            System.out.println("Unable to connect to database");
            return;
        }

        btnSignup.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                String email = textEmail.getText();
                String password = textPassword.getText();
                String confirmPassword = textConfirmPassword.getText();
                boolean success = signup(email, password, confirmPassword);
                if (success) {
                    App.getAppInstance().navigate("login");
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

    private boolean signup(String email, String password, String confirmPassword) {
        if (conn == null) return false;

        if (!password.equals(confirmPassword)) {
            App.getAppInstance().alert("Passwords do not match");
            return false;
        }

        User user = UserDAO.getUser(email);
        if (user == null) {
            String hashed = Hasher.hash(password);
            UserDAO.insertUser(email, hashed);
            return true;
        } else {
            App.getAppInstance().alert("Email already exists");
            return false;
        }
    }
}
