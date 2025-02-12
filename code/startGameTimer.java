import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class startGameTimer {
    private JLabel timerLabel; // Label to display elapsed time
    private Timer gameTimer; // Swing Timer (updates every second)
    private long startTime; // Record the start time
    private boolean isRunning; // To track if the timer is running

    public startGameTimer() {
        timerLabel = new JLabel("Time: 00:00");
        isRunning = false; // Timer is initially not running
    }

    public void startGameTimer() {
        if (isRunning)
            return; // Prevent restarting if already running

        startTime = System.currentTimeMillis(); // Record the game start time
        isRunning = true;

        // Create a new timer that updates every second
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                timerLabel.setText("Time: " + formatTime(elapsedMillis));
            }
        });
        gameTimer.start();
    }

    private String formatTime(long millis) {
        long totalSeconds = millis / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        return (hours > 0) ? String.format("%02d:%02d:%02d", hours, minutes, seconds)
                : String.format("%02d:%02d", minutes, seconds);
    }

    public void LoseGame() {
        if (gameTimer != null && isRunning) {
            gameTimer.stop();
            isRunning = false;
        }
        JOptionPane.showMessageDialog(null, "Game Over! Final Time: " + timerLabel.getText());
    }

    public void checkWin() {
        if (gameTimer != null && isRunning) {
            gameTimer.stop();
            isRunning = false;
        }
        JOptionPane.showMessageDialog(null, "Yeah, you won! Final Time: " + timerLabel.getText());
    }

    public void resetTimer() {
        if (gameTimer != null && isRunning) {
            gameTimer.stop();
        }
        isRunning = false;
        startGameTimer(); // Restart the timer
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }
}
