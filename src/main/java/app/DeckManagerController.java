package app;

import db.DAO.FlashcardDAO;
import db.Flashcard;
import java.util.List;
import java.util.ArrayList;
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

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DeckManagerController implements Initializable {

    public static class DeckCell {
        //private final SimpleStringProperty date;
        private final SimpleStringProperty name;
        private final SimpleStringProperty description;
        private final int deckId;

        /**
         * Class Constructor for DeckCell
         * @param name The name of the deck.
         * @param description The description of the deck.
         * @param deckId The unique identifier for the deck.
         */
        public DeckCell(String name, String description, int deckId) {
            //this.date = new SimpleStringProperty(date);
            this.name = new SimpleStringProperty(name);
            this.description = new SimpleStringProperty(description);
            this.deckId = deckId;
        }

        /*public String getDate() {
            return date.get();
        }*/

        /**
         * Returns the deck cell name
         * @return deck cell name
         */
        public String getName() {
            return name.get();
        }

        /**
         * Returns the deck cell description
         * @return deck cell description
         */
        public String getDescription() {
            return description.get();
        }

        /**
         * Returns the deck cell unique identifier
         * @return deck cell identifier
         */
        public int getDeckId() {
            return deckId;
        }
    }

    @FXML
    private TableView<DeckCell> tableDecks;

    //@FXML
    //private TableColumn<DeckCell, String> dateColumn;

    @FXML
    private TableColumn<DeckCell, String> nameColumn;

    @FXML
    private TableColumn<DeckCell, String> descriptionColumn;

    @FXML
    private TableColumn<DeckCell, Void> deleteColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        Deck[] decks = DeckDAO.getAllDecks();
        ObservableList<DeckCell> decksList = FXCollections.observableArrayList();
        for (Deck deck : decks) {
            DeckCell cell = new DeckCell(deck.getTitle(), deck.getDescription(), deck.getId());
            decksList.add(cell);
        }

        tableDecks.setItems(decksList);

        // ✅ Add this to handle double-click on row
        tableDecks.setRowFactory(tv -> {
            TableRow<DeckCell> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    DeckCell clickedDeck = row.getItem();
                    openDeckView(clickedDeck);
                }
            });
            return row;
        });

        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/fxml/images/dustbin.jpg")));
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
                deleteButton.setGraphic(imageView);
                deleteButton.setStyle("-fx-background-color: transparent;");
                deleteButton.setOnAction(event -> {
                    DeckCell cell = getTableView().getItems().get(getIndex());
                    boolean success = DeckDAO.deleteDeckById(cell.getDeckId());
                    if (success) {
                        getTableView().getItems().remove(cell);
                        System.out.println("Deck deleted: " + cell.getName());
                    } else {
                        System.out.println("Failed to delete deck with ID: " + cell.getDeckId());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

    }

    /**
     * This opens DeckView.fxml and passes deck name + date
     * In your DeckManagerController, modify the openDeckView method:
     * @param deck deck cell to open
     */
    private void openDeckView(DeckCell deck) {
        try {
            // Get DUE flashcards for this deck
            List<Flashcard> flashcards = FlashcardDAO.getDueFlashcards(deck.getDeckId());

            if (flashcards.isEmpty()) {
                System.out.println("No due flashcards in this deck");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_1.fxml"));
            Parent root = loader.load();

            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcards(flashcards);

            Stage stage = (Stage) tableDecks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reviewing: " + deck.getName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            List<Flashcard> flashcards = FlashcardDAO.getFlashcardsByDeckId(deck.getDeckId());

            if (flashcards.isEmpty()) {
                System.out.println("No flashcards in this deck");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Review_Flashcards_1.fxml"));
            Parent root = loader.load();

            ReviewFlashcardController controller = loader.getController();
            controller.setFlashcards(flashcards); // This now sets the static list

            Stage stage = (Stage) tableDecks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reviewing: " + deck.getName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * This method is called when the user clicks on the dashboard button.
     * It redirects the user back to the dashboard page.
     * @param event click event thrown
     */
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

    /**
     * Redirects the user to the login page.
     * @param event action event thrown
     */
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

