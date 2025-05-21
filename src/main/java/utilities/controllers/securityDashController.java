package utilities.controllers;
import db.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.models.Session;

public class securityDashController {
    @FXML
    private void loginHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login_History.fxml"));
            Parent root = loader.load();

            LoginHistoryController controller = loader.getController();
            controller.setUser(Session.getCurrentUser());

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login History");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
