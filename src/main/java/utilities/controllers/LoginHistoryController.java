package utilities.controllers;

import db.DBConnector;
import db.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.models.LoginHistory;
import utilities.services.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * JavaFX controller for displaying the login history of a user.
 * Retrieves login timestamps from the database and shows them in a table.
 */
public class LoginHistoryController {

    @FXML private TableView<LoginHistory> loginTable;
    @FXML private TableColumn<LoginHistory, String> dateColumn;
    @FXML private TableColumn<LoginHistory, String> timeColumn;

    /**
     * Initializes the login history table with date and time columns.
     * Automatically loads the current user's login history.
     */
    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadLoginHistory(currentUser.getId());
        } else {
            System.out.println("No user in session – cannot load login history.");
        }
    }

    /**
     * Loads the login history entries from the database for the given user.
     *
     * @param userId the ID of the user whose login history should be loaded
     */
    public void loadLoginHistory(int userId) {
        ObservableList<LoginHistory> history = FXCollections.observableArrayList();

        String sql = "SELECT timestamp FROM login_log WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] parts = rs.getString("timestamp").split(" ");
                String date = parts.length > 0 ? parts[0] : "";
                String time = parts.length > 1 ? parts[1] : "";
                history.add(new LoginHistory(date, time));
            }

            loginTable.setItems(history);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
