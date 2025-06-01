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
import utilities.services.UserSession;

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
     * <p>
     * Wraps deck properties for display in the JavaFX TableView columns.
     * </p>
     */
    public static class DeckCell {
        /** The name of the deck */
        private final SimpleStringProperty name;

        /** The description of the deck */
        private final SimpleStringProperty description;

        /** The unique identifier of the deck */
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
         * Gets the deck name.
         * @return the deck name
         */
        public String getName() {
            return name.get();
        }

        /**
         * Gets the deck description.
         * @return the deck description
         */
        public String getDescription() {
            return description.get();
        }

        /**
         * Gets the deck ID.
         * @return the deck identifier
         */
        public int getDeckId() {
            return deckId;
        }
    }

    /** TableView component displaying the list of decks */
    @FXML
    private TableView<DeckCell> tableDecks;

    /** TableColumn for displaying deck names */
    @FXML
    private TableColumn<DeckCell, String> nameColumn;

    /** TableColumn for displaying deck descriptions */
    @FXML
    private TableColumn<DeckCell, String> descriptionColumn;

    /** TableColumn containing delete buttons for each deck */
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
        // Configure column value factories
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load decks from database
        Deck[] decks = DeckDAO.getAllDecks();
        ObservableList<DeckCell> decksList = FXCollections.observableArrayList();
        for (Deck deck : decks) {
            decksList.add(new DeckCell(deck.getTitle(), deck.getDescription(), deck.getId()));
        }
        tableDecks.setItems(decksList);

        // Configure row click handler
        tableDecks.setRowFactory(tv -> {
            TableRow<DeckCell> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    openDeckView(row.getItem());
                }
            });
            return row;
        });

        // Configure delete column with buttons
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                // Set up delete button with icon
                ImageView imageView = new ImageView(
                        new Image(getClass().getResourceAsStream("/fxml/images/dustbin.jpg")));
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
                deleteButton.setGraphic(imageView);
                deleteButton.setStyle("-fx-background-color: transparent;");

                // Set delete action
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
            controller.setDeckName(deck.getName());

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

            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {

        try {
            System.out.println("From Dashboard → Current user: " + UserSession.getInstance().getCurrentUser());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Account.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Account Dashboard");
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