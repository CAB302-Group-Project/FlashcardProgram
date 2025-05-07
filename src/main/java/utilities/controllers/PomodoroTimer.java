package utilities.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer {
    @FXML
    private Text textPause;

    @FXML
    private Text textTimer;

    private int seconds;
    private boolean paused;
    private Timer timer;

    public PomodoroTimer() {
        PomodoroTimer instance = this;
        paused = true;
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
}
