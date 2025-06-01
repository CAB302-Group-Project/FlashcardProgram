package utilities.controllers;

import app.FlashcardApp;
import db.DAO.FlashcardDAO;
import db.DAO.PomoDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class TrackProgressController {

    @FXML
    private BarChart<String, Number> QuizPerformance;

    @FXML
    private StackedBarChart<Number, String> FlashcardMemory;

    @FXML
    private Button dashboardbutton;

    @FXML
    private Button profilebutton;

    @FXML
    private Text quizCountText;

    @FXML
    private Text pomodoroSessionCount;

    @FXML
    public void initialize() {
        populateQuizPerformance();
        populateFlashcardMemory();
        updateStats();
    }

    private void populateQuizPerformance() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Correct");

        // Sample data, replace with DB values
        series.getData().add(new XYChart.Data<>("Math", 8));
        series.getData().add(new XYChart.Data<>("History", 6));
        series.getData().add(new XYChart.Data<>("Science", 7));

        QuizPerformance.getData().clear();
        QuizPerformance.getData().add(series);
    }

    private void populateFlashcardMemory() {
        FlashcardMemory.getData().clear();

        XYChart.Series<Number, String> easySeries = new XYChart.Series<>();
        easySeries.setName("Easy");
        easySeries.getData().add(new XYChart.Data<>(15, "Memory"));

        XYChart.Series<Number, String> mediumSeries = new XYChart.Series<>();
        mediumSeries.setName("Medium");
        mediumSeries.getData().add(new XYChart.Data<>(10, "Memory"));

        XYChart.Series<Number, String> hardSeries = new XYChart.Series<>();
        hardSeries.setName("Hard");
        hardSeries.getData().add(new XYChart.Data<>(5, "Memory"));

        FlashcardMemory.getData().addAll(easySeries, mediumSeries, hardSeries);
    }

    private void updateStats() {
        int userId = FlashcardApp.getInstance().getUserId();
        int reviewedFlashcards = FlashcardDAO.countReviewedFlashcards(userId);
        int pomodorosThisWeek = PomoDao.countSessionsThisWeek(userId);

        quizCountText.setText(String.valueOf(reviewedFlashcards));
        pomodoroSessionCount.setText(String.valueOf(pomodorosThisWeek));
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Account.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
