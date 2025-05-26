package app;

import db.DAO.FlashcardDAO;
import db.Flashcard;
import java.util.List;
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

/**
 * Controller for managing decks in the flashcard application.
 * <p>
 * This JavaFX controller handles:
 * <ul>
 *   <li>Displaying all decks in a TableView</li>
 *   <li>Deck selection and navigation to review sessions</li>
 *   <li>Deck deletion functionality</li>
 *   <li>Navigation to dashboard and login screens</li>
 * </ul>
 * </p>
 *
 * <p><b>UI Components:</b>
 * <ul>
 *   <li>TableView showing deck names and descriptions</li>
 *   <li>Delete buttons for each deck row</li>
 *   <li>Click handlers for deck selection</li>
 * </ul>
 * </p>
 *
 * @author Jasmine & Della
 * @version 1.0
 * @see Deck
 * @see DeckDAO
 * @see Initializable
 * @since 1.0
 */

public class DeckManagerController implements Initializable {
    /**
     * Data model class for representing deck information in the TableView.
     */
    public static class DeckCell {
        private final SimpleStringProperty name;
        private final SimpleStringProperty description;
        private final int deckId;

        /**
         * Creates a new DeckCell with the specified properties.
         *
         * @param name the deck name
         * @param description the deck description
         * @param deckId the unique deck identifier
         */
        public DeckCell(String name, String description, int deckId) {
            this.name = new SimpleStringProperty(name);
            this.description = new SimpleStringProperty(description);
            this.deckId = deckId;
        }

        /**
         * @return the deck name, description and deck id
         */
        public String getName() {
            return name.get();
        }

        public String getDescription() {
            return description.get();
        }

        public int getDeckId() {
            return deckId;
        }
    }

    @FXML
    private TableView<DeckCell> tableDecks;

    @FXML
    private TableColumn<DeckCell, String> nameColumn;

    @FXML
    private TableColumn<DeckCell, String> descriptionColumn;

    @FXML
    private TableColumn<DeckCell, Void> deleteColumn;

    /**
     * Initializes the controller after FXML loading.
     * <p>
     * Sets up:
     * <ul>
     *   <li>TableView column bindings</li>
     *   <li>Deck data loading from database</li>
     *   <li>Row click handlers</li>
     *   <li>Delete button configuration</li>
     * </ul>
     * </p>
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        Deck[] decks = DeckDAO.getAllDecks();
        ObservableList<DeckCell> decksList = FXCollections.observableArrayList();
        for (Deck deck : decks) {
            DeckCell cell = new DeckCell(deck.getTitle(), deck.getDescription(), deck.getId());
            decksList.add(cell);
        }

        tableDecks.setItems(decksList);

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
     * Opens the flashcard review view for the selected deck.
     * <p>
     * Loads all due flashcards for the deck and transitions to the review interface.
     * Only shows flashcards that are due for review based on spaced repetition scheduling.
     * </p>
     *
     * @param deck the deck to review
     * @see FlashcardDAO#getDueFlashcards(int)
     * @see ReviewFlashcardController
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
    }

    /**
     * Handles navigation to the dashboard view.
     * <p>
     * Loads the dashboard for the currently logged-in user.
     * Falls back to login view if no user is authenticated.
     * </p>
     *
     * @param event the ActionEvent that triggered this navigation
     * @see DashboardController
     * @see UserDAO
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
     * Handles navigation to the login view.
     * <p>
     * Clears any existing user session and returns to the login screen.
     * </p>
     *
     * @param event the ActionEvent that triggered this navigation
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

