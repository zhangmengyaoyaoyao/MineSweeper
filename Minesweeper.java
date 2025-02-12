import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Minesweeper implements ActionListener {
    JFrame frame = new JFrame("MineSweeper");
    JButton resetButton = new JButton("Restart");
    Container container = new Container();

    int row, col, bombCount;
    JButton[][] buttons;
    int[][] counts;
    final int BOMBCODE = 10;

    public Minesweeper() {
        frame.setSize(900, 900);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] options = { "Beginner", "Intermediate", "Expert" };
        int choice = JOptionPane.showOptionDialog(frame, "Select Difficulty Level", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                row = 9; col = 9; bombCount = 10;
                break;
            case 1:
                row = 16; col = 16; bombCount = 30;
                break;
            case 2:
                row = 16; col = 30; bombCount = 50;
                break;
            default:
                row = 9; col = 9; bombCount = 10;
        }

        buttons = new JButton[row][col];
        counts = new int[row][col];

        addResetButton();
        addButtons();
        placeBombs();
        calculateAdjacentBombs();

        frame.setVisible(true);
    }

    public void addResetButton() {
        resetButton.setBackground(Color.WHITE);
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
                i--;
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
                button.setBackground(Color.LIGHT_GRAY);  // 未翻开时为浅灰色
                button.addActionListener(this);
                buttons[i][j] = button;
                container.add(button);
            }
        }
    }

    public void calculateAdjacentBombs() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (counts[i][j] == BOMBCODE) continue;
                int count = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        int newRow = i + x, newCol = j + y;
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
                buttons[i][j].setBackground(Color.LIGHT_GRAY); // 重置回未翻开状态
                counts[i][j] = 0;
            }
        }
        placeBombs();
        calculateAdjacentBombs();
    }

    void handleCellClick(JButton button) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (button.equals(buttons[i][j])) {
                    if (counts[i][j] == BOMBCODE) {
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
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (buttons[i][j].isEnabled() && counts[i][j] != BOMBCODE) return;
            }
        }
        JOptionPane.showMessageDialog(frame, "🎉 Congratulations! You won!");
    }

    void revealCell(int i, int j) {
        if (!buttons[i][j].isEnabled()) return;

        buttons[i][j].setEnabled(false);
        buttons[i][j].setBackground(Color.DARK_GRAY); // 翻开后变为深灰色

        if (counts[i][j] > 0) {
            buttons[i][j].setText(String.valueOf(counts[i][j]));
        } else {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    int newRow = i + x, newCol = j + y;
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
                    buttons[i][j].setText("💣");
                    buttons[i][j].setBackground(Color.RED);
                } else if (counts[i][j] > 0) {
                    buttons[i][j].setText(String.valueOf(counts[i][j]));
                }
                buttons[i][j].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(frame, "💥 Game Over! You hit a bomb.");
    }

    public static void main(String[] args) {
        new Minesweeper();
    }
}
