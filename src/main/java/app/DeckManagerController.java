package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

import db.DeckDAO;
import db.Deck;
import javafx.scene.control.cell.PropertyValueFactory;

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
    }
}
