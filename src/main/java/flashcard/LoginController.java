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

public class LoginController implements Initializable {
    @FXML
    private TextField textEmail;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnHome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                String email = textEmail.getText();
                String password = textPassword.getText();
                App app = App.getAppInstance();

                if (app.getSession().login(email, password)) {
                    app.navigate("dashboard");
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
