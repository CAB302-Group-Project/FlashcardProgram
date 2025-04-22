package flashcard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label textWelcome;

    @FXML
    private Button btnLogout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = App.getAppInstance().getSession();
        String email = session.getEmail();
        textWelcome.setText("Welcome, " + email);

        btnLogout.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                session.logout();
                App.getAppInstance().navigate("login");
            }
        });
    }
}
