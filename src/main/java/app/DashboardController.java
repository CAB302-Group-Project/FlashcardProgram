package app;

import com.example.flashcardai.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Text textName;

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = FlashcardApp.getInstance().getSession();
        if (currentUser != null) {
            textName.setText(currentUser.getEmail());
        }
    }

    @FXML
    private void handleCreateDeck(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Create_Deck.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Deck");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
