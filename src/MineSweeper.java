import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class MineSweeper implements ActionListener {
    JFrame frame = new JFrame("MineSweeper");
    JButton resetButton = new JButton("Restart");
    Container container = new Container();

    // Game settings
    int row;
    int col;
    int bombCount;
    JButton[][] buttons;
    int[][] counts;
    final int BOMBCODE = 10;

    // Timer functionality
    private startGameTimer gameTimer; // Timer object

    // For First Click Safety
    private boolean firstClick = true; // Track if it's the first click

    // Constructor
    public MineSweeper() {
        // Set up the window
        frame.setSize(900, 900);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize the game timer
        gameTimer = new startGameTimer(); // Create a new timer object
        frame.add(gameTimer.getTimerLabel(), BorderLayout.SOUTH); // Add timer label at the bottom
        gameTimer.startGameTimer(); // Start the game timer immediately when the game starts

        // Add difficulty levels
        String[] options = { "Beginner", "Intermediate", "Expert" };
        int choice = JOptionPane.showOptionDialog(frame, "Select Difficulty Level", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // Beginner
                row = 9;
                col = 9;
                bombCount = 10;
                break;
            case 1: // Intermediate
                row = 16;
                col = 16;
                bombCount = 30;
                break;
            case 2: // Expert
                row = 30;
                col = 16;
                bombCount = 50;
                break;
            default:
                row = 9;
                col = 9;
                bombCount = 10;
                break;
        }

        buttons = new JButton[row][col];
        counts = new int[row][col];


        // Add restart button
        addResetButton();

        // Add game buttons
        addButtons();

        // Place bombs
        placeBombs();

        // Calculate adjacent bomb counts
        calculateAdjacentBombs();

        frame.setVisible(true);
    }

    public void addResetButton() {
        resetButton.setBackground(Color.WHITE);//look1
        resetButton.setOpaque(true);
        resetButton.addActionListener(this);
        frame.add(resetButton, BorderLayout.NORTH);
    }

    public void placeBombs() {
        Random rand = new Random();
        int randRow, randCol;
        for (int i = 0; i < bombCount; i++) {
            randRow = rand.nextInt(row);
            randCol = rand.nextInt(col);
            if (counts[randRow][randCol] == BOMBCODE) {
                i--; // Avoid placing bombs at the same position
            } else {
                counts[randRow][randCol] = BOMBCODE;
            }
        }
    }

    public void addButtons() {
        frame.add(container, BorderLayout.CENTER);
        container.setLayout(new GridLayout(row, col));
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                JButton button = new JButton();
                button.setBackground(Color.lightGray);
                button.setOpaque(true);
                button.addActionListener(this);
                buttons[i][j] = button;
                container.add(button);
            }
        }
    }

    public void calculateAdjacentBombs() {
        int count;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                count = 0;
                if (counts[i][j] == BOMBCODE) continue;

                // Check surrounding cells
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        int newRow = i + x;
                        int newCol = j + y;
                        if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col && counts[newRow][newCol] == BOMBCODE) {
                            count++;
                        }
                    }
                }
                counts[i][j] = count;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.equals(resetButton)) {
            resetGame();
        } else {
            handleCellClick(button);
        }
    }

    void resetGame() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                counts[i][j] = 0;
            }
        }
        placeBombs();
        calculateAdjacentBombs();
        firstClick = true; // Reset the first click flag
        gameTimer.resetTimer(); // Reset the timer when the game restarts
    }

    void handleCellClick(JButton button) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (button.equals(buttons[i][j])) {
                    if(counts[i][j] == BOMBCODE && firstClick){
                        while (counts[i][j] == BOMBCODE){
                            placeBombs(); // Place bombs, avoiding first click area
                            calculateAdjacentBombs();
                        }
                        firstClick = false;
                    }
                    if (counts[i][j] == BOMBCODE) {
                        endGame();
                    } else {
                        revealCell(i, j);
                        checkWin();
                    }
                    firstClick = false;
                    SoundEffects.clickSound();
                    return;
                }
            }
        }
    }

    void checkWin() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (buttons[i][j].isEnabled() && counts[i][j] != BOMBCODE) return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Congratulations! You won!");
        SoundEffects.winSound();
        gameTimer.checkWin(); // Stop timer and display time when the player wins
    }

    void revealCell(int i, int j) {
        if (!buttons[i][j].isEnabled()) return;

        buttons[i][j].setEnabled(false);
        buttons[i][j].setText(counts[i][j] + "");


        if (counts[i][j] == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    int newRow = i + x;
                    int newCol = j + y;
                    if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col && counts[newRow][newCol] != BOMBCODE) {
                        revealCell(newRow, newCol);
                    }
                }
            }
        }
    }

    void endGame() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (counts[i][j] == BOMBCODE) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackground(Color.PINK);
                } else {
                    buttons[i][j].setText(counts[i][j] + "");
                }
                buttons[i][j].setEnabled(false);
            }
        }
        SoundEffects.loseSound();
        JOptionPane.showMessageDialog(frame, "Game Over! You hit a bomb.");
        gameTimer.LoseGame(); // Stop timer and display time when the player loses
    }

    public static void main(String[] args) {
        new MineSweeper();
    }
}
