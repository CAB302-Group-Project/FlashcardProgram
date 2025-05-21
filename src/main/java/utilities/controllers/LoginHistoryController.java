package utilities.controllers;

import db.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utilities.models.LoginHistory;
import db.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginHistoryController {
    private User user;

    @FXML private TableView<LoginHistory> loginTable;
    @FXML private TableColumn<LoginHistory, String> dateColumn;
    @FXML private TableColumn<LoginHistory, String> timeColumn;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            loadLoginHistory(user.getId());
        }
    }

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
