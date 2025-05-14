package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TrackProgressController {

    @FXML private BarChart<?, ?> QuizPerformance;
    @FXML private StackedBarChart<?, ?> FlashcardMemory;
    @FXML private Button dashboardbutton;
    @FXML private Button profilebutton;

    @FXML
    public void initialize() {
        // populate charts, attach listeners, etc.
    }

    // UI Components from FXML
    /*@FXML private Button dashboardButton;
    @FXML private Button profileButton;

    @FXML private BarChart<String, Number> quizPerformance;
    @FXML private CategoryAxis quizCategoryAxis;
    @FXML private NumberAxis quizQuestionsAxis;

    @FXML private Text pomodoroSessionsText;
    @FXML private Text completedQuizzesText;
    @FXML private Text completedQuizzesCount;

    @FXML private StackedBarChart<Number, String> flashcardMemory;
    @FXML private NumberAxis memoryXAxis;
    @FXML private CategoryAxis memoryYAxis;

    @FXML private Circle easyCircle;
    @FXML private Circle mediumCircle;
    @FXML private Circle hardCircle;

    @FXML
    public void initialize() {
        // Initialize chart data and other components
        setupQuizPerformanceChart();
        setupFlashcardMemoryChart();
        updateProgressStats();
    }

    private void setupQuizPerformanceChart() {
        quizCategoryAxis.setLabel("Category");
        quizQuestionsAxis.setLabel("Questions");
        // Add sample data or load from your data source
    }

    private void setupFlashcardMemoryChart() {
        memoryXAxis.setLabel("Count");
        memoryYAxis.setLabel("Difficulty Level");
        // Add sample data or load from your data source
    }

    private void updateProgressStats() {
        // Update the text fields with actual data
        // These would typically come from your service layer
        pomodoroSessionsText.setText("5"); // Example value
        completedQuizzesCount.setText("12"); // Example value
    }

    @FXML
    private void handleDashboardButton() {
        // Handle navigation back to dashboard
        // Example: FlashcardApp.getInstance().showDashboard();
    }

    @FXML
    private void handleProfileButton() {
        // Handle profile button click
        // Example: FlashcardApp.getInstance().showProfile();
    }*/
}