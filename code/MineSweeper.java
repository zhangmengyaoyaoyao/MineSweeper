import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer; // Import Timer

public class MineSweeper implements ActionListener {
    JFrame frame = new JFrame("MineSweeper");
    JButton resetButton = new JButton("Restart");
    Container container = new Container();

    // Game settings
    final int ROWS = 20;
    final int COLS = 20;
    final int BOMB_COUNT = 30;
    JButton[][] buttons = new JButton[ROWS][COLS];
    int[][] cellValues = new int[ROWS][COLS];
    final int BOMB_CODE = 10;

    // Timer functionality
    private startGameTimer gameTimer; // Timer object

    // Constructor
    public MineSweeper() {

        Locale.setDefault(Locale.ENGLISH);

        // Set up the window
        frame.setSize(900, 900);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialize the game timer
        gameTimer = new startGameTimer(); // Create a new timer object
        frame.add(gameTimer.getTimerLabel(), BorderLayout.SOUTH); // Add timer label at the bottom
        gameTimer.startGameTimer(); // Start the game timer immediately when the game starts

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
        resetButton.setBackground(Color.WHITE);
        resetButton.setOpaque(true);
        resetButton.addActionListener(this);
        frame.add(resetButton, BorderLayout.NORTH);
    }

    public void placeBombs() {
        Random rand = new Random();
        int randRow, randCol;
        for (int i = 0; i < BOMB_COUNT; i++) {
            randRow = rand.nextInt(ROWS);
            randCol = rand.nextInt(COLS);
            if (cellValues[randRow][randCol] == BOMB_CODE) {
                i--; // Avoid placing bombs at the same position
            } else {
                cellValues[randRow][randCol] = BOMB_CODE;
            }
        }
    }

    public void addButtons() {
        frame.add(container, BorderLayout.CENTER);
        container.setLayout(new GridLayout(ROWS, COLS));
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JButton button = new JButton();
                button.setBackground(Color.YELLOW);
                button.setOpaque(true);
                button.addActionListener(this);
                buttons[i][j] = button;
                container.add(button);
            }
        }
    }

    public void calculateAdjacentBombs() {
        int count;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                count = 0;
                if (cellValues[i][j] == BOMB_CODE)
                    continue;

                // Check surrounding cells
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        int newRow = i + x;
                        int newCol = j + y;
                        if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                                && cellValues[newRow][newCol] == BOMB_CODE) {
                            count++;
                        }
                    }
                }

                cellValues[i][j] = count;
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
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.YELLOW);
                cellValues[i][j] = 0;
            }
        }
        placeBombs();
        calculateAdjacentBombs();
        gameTimer.resetTimer(); // Reset the timer when the game restarts
    }

    void handleCellClick(JButton button) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (button.equals(buttons[i][j])) {
                    if (cellValues[i][j] == BOMB_CODE) {
                        endGame();
                    } else {
                        revealCell(i, j);
                        checkWin();
                    }
                    return;
                }
            }
        }
    }

    void checkWin() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (buttons[i][j].isEnabled() && cellValues[i][j] != BOMB_CODE)
                    return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Congratulations! You won!");
        gameTimer.checkWin(); // Stop timer and display time when the player wins
    }

    void revealCell(int i, int j) {
        if (!buttons[i][j].isEnabled())
            return;

        buttons[i][j].setEnabled(false);
        buttons[i][j].setText(cellValues[i][j] + "");

        if (cellValues[i][j] == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    int newRow = i + x;
                    int newCol = j + y;
                    if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS
                            && cellValues[newRow][newCol] != BOMB_CODE) {
                        revealCell(newRow, newCol);
                    }
                }
            }
        }
    }

    void endGame() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (cellValues[i][j] == BOMB_CODE) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackground(Color.RED);
                } else {
                    buttons[i][j].setText(cellValues[i][j] + "");
                }
                buttons[i][j].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(frame, "Game Over! You hit a bomb.");
        gameTimer.LoseGame(); // Stop timer and display time when the player loses
    }

    public static void main(String[] args) {
        new MineSweeper();
    }
}
