package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class PomodoroPopOutController {
    @FXML
    private Text textPopoutTimer;

    private final Timer timer;
    private int seconds;
    private boolean paused;

    public PomodoroPopOutController() {
        this(0);
    }

    public PomodoroPopOutController(int seconds) {
        PomodoroPopOutController instance = this;
        this.seconds = seconds;
        this.paused = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!instance.paused) {
                    if (instance.seconds == 0) instance.paused = true;

                    int minutes = instance.seconds / 60;
                    int seconds = instance.seconds % 60;
                    textPopoutTimer.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    instance.seconds -= 1;
                }
            }
        }, 1000, 1000);
    }

    public void handlePopoutToggle() {
        paused = !paused;
    }

    public void handleMaximize(ActionEvent event) {
        try {
            handlePopoutToggle();
            timer.cancel();
            timer.purge();

            PomodoroTimerController controller = new PomodoroTimerController(seconds);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Pomodoro_Timer.fxml"));
            loader.setController(controller);
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pomodoro");
            stage.show();
            controller.handleToggle();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
