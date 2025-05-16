package app;

import db.DAO.DeckDAO;
import db.DAO.UserDAO;
import db.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilities.controllers.DashboardController;

import java.net.URL;
import java.util.ResourceBundle;

import db.Deck;
import utilities.controllers.ReviewFlashcardController;

public class DeckManagerController implements Initializable {

    public static class DeckCell {
        private final SimpleStringProperty date;
        private final SimpleStringProperty name;

        public DeckCell(String date, String name) {
            this.date = new SimpleStringProperty(date);
            this.name = new SimpleStringProperty(name);
        }

        public String getDate() {
            return date.get();
        }

        public String getName() {
            return name.get();
        }
    }

    @FXML
    private TableView<DeckCell> tableDecks;

    @FXML
    private TableColumn<DeckCell, String> dateColumn;

    @FXML
    private TableColumn<DeckCell, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        Deck[] decks = DeckDAO.getAllDecks();
        ObservableList<DeckCell> decksList = FXCollections.observableArrayList();
        for (Deck deck : decks) {
            DeckCell cell = new DeckCell(deck.getCreatedAt(), deck.getTitle());
            decksList.add(cell);
        }

        tableDecks.setItems(decksList);

        // ✅ Add this to handle double-click on row
        tableDecks.setRowFactory(tv -> {
            TableRow<DeckCell> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    DeckCell clickedDeck = row.getItem();
                    openDeckView(clickedDeck);
                }
            });
            return row;
        });
    }

    // ✅ This opens DeckView.fxml and passes deck name + date
    private void openDeckView(DeckCell deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_1.fxml"));
            Parent root = loader.load();

            //ReviewFlashcardController controller = loader.getController();
            //controller.initData(deck.getName(), deck.getDate());

            Stage stage = (Stage) tableDecks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Deck: " + deck.getName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDashboard(ActionEvent event) {
        try {
            int currentUserId = FlashcardApp.getInstance().getUserId();
            if (currentUserId == -1) {
                System.out.println("User not found");
                handleLogin(event);
                return;
            }

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            User user = UserDAO.getUserById(currentUserId);
            if (user == null) {
                System.out.println("User not found 2");
                handleLogin(event);
                return;
            }

            controller.setUser(user);
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
