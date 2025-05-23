package utilities.controllers;

import app.FlashcardApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class StartQuizController {

    public Button backButton;
    @FXML
    private Spinner<Integer> questionCountSpinner;

    @FXML
    private ChoiceBox<String> topicChoiceBox;

    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    @FXML
    private Button startButton;

    @FXML
    public void initialize() {
        // Initialize spinner for number of questions (5-20 in steps of 5)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 10, 15, 20);
        questionCountSpinner.setValueFactory(valueFactory);

        // Initialize topic choices
        topicChoiceBox.getItems().addAll(
                "General Knowledge",
                "Science",
                "History",
                "Programming",
                "Mathematics"
        );
        topicChoiceBox.setValue("Choose Topic"); // Default selection

        // Initialize difficulty choices
        difficultyChoiceBox.getItems().addAll(
                "Easy",
                "Medium",
                "Hard"
        );
        difficultyChoiceBox.setValue("Choose Difficulty"); // Default selection
    }

    @FXML
    private void handleStartQuiz(ActionEvent event) {
        // Get selected values
        int questionCount = questionCountSpinner.getValue();
        String topic = topicChoiceBox.getValue();
        String difficulty = difficultyChoiceBox.getValue();

        System.out.println("Starting quiz with:");
        System.out.println("Questions: " + questionCount);
        System.out.println("Topic: " + topic);
        System.out.println("Difficulty: " + difficulty);

        // Here you would typically:
        // 1. Generate or fetch quiz questions based on selections
        // 2. Navigate to the quiz screen

        // For now, just close the window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FlashcardApp.getInstance().setSessionToken(null);
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}