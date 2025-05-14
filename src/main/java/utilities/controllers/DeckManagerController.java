package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class DeckManagerController {

    @FXML private TableView<?> tableDecks;
    @FXML private TableColumn<?, ?> dateColumn;
    @FXML private TableColumn<?, ?> nameColumn;
    @FXML private TableColumn<?, ?> actionsColumn;

    @FXML
    private void handleBackToDashboard() {
        // Implement navigation back to dashboard
    }

    @FXML
    private void handleProfile() {
        // Implement profile view opening
    }

    @FXML
    public void initialize() {
        // Configure table columns and load data
    }
}