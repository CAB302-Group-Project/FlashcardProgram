package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

import db.User;
import db.DAO.UserDAO;
import utilities.controllers.DashboardController;

public class PomodoroTimerController {
    @FXML
    private Text textPause;

    @FXML
    private Text textTimer;

    private final Timer timer;
    private int seconds;
    private boolean paused;

    public PomodoroTimerController() {
        this(0);
    }

    public PomodoroTimerController(int seconds) {
        PomodoroTimerController instance = this;
        this.seconds = seconds;
        this.paused = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!instance.paused) {
                    if (instance.seconds == 0) instance.paused = true;

                    int minutes = instance.seconds / 60;
                    int seconds = instance.seconds % 60;
                    textTimer.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    instance.seconds -= 1;
                }
            }
        }, 1000, 1000);
    }

    public void handleFocus() {
        seconds = 1500;
        handleToggle();
    }

    public void handleShort() {
        seconds = 300;
        handleToggle();
    }

    public void handleLong() {
        seconds = 1800;
        handleToggle();
    }

    public void handleToggle() {
        paused = !paused;
        if (paused) textPause.setText("START");
        else textPause.setText("PAUSE");
    }

    public void handlePopout(MouseEvent event) {
        try {
            handleToggle();
            timer.cancel();
            timer.purge();

            PomodoroPopOutController controller = new PomodoroPopOutController(seconds);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Pomodoro_Pop-Out.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pomodoro Pop Out");
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /*public void handleDashboard(ActionEvent event) {
        try {
            handleToggle();
            timer.cancel();
            timer.purge();

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
    }*/

    public void handleLogin (ActionEvent event) {
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