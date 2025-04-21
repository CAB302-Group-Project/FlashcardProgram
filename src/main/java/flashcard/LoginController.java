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

import db.User;
import db.UserDAO;

public class LoginController implements Initializable {
    @FXML
    private TextField textEmail;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnHome;

    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = db.DBConnector.connect();
        if (conn == null) {
            System.out.println("Unable to connect to database");
            return;
        }

        btnLogin.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                String email = textEmail.getText();
                String password = textPassword.getText();
                String token = login(email, password);
                if (token != null) {
                    App.getAppInstance().setToken(token);
                    App.getAppInstance().navigate("dashboard");
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

    private String login(String email, String password) {
        if (conn == null) return null;

        User user = UserDAO.authUser(email, password);
        if (user != null) {
            // TODO: Generate token using email and with expiration
            return user.getEmail();
        }

        App.getAppInstance().alert("Unable to authenticate user");
        return null;
    }
}
