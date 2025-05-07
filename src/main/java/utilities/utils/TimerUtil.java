/*package com.example.flashcardai.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerUtil {
    public static void startTimer(int seconds, Runnable onFinish) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds), e -> onFinish.run()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}*/


/*package com.example.flashcardai.utils;

public class TimerUtil {
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public String getFormattedElapsedTime() {
        long elapsed = getElapsedTime();
        return String.format("%d min, %d sec",
                elapsed / 60000, (elapsed % 60000) / 1000);
    }
}*/

package utilities.utils;

public class TimerUtil {
    private long startTime;
    private long pausedTime;
    private boolean isRunning;

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void pause() {
        if (isRunning) {
            pausedTime = System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void resume() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - pausedTime;
            isRunning = true;
        }
    }

    public long getElapsedTimeMillis() {
        return isRunning
                ? System.currentTimeMillis() - startTime
                : pausedTime;
    }

    public String getFormattedTime() {
        long elapsed = getElapsedTimeMillis() / 1000;
        long minutes = elapsed / 60;
        long seconds = elapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}