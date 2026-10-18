import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A two-player Tic-Tac-Toe game built with Swing.
 *
 * @author Akash Bhatt
 * Roll number: 2607
 */
public class TicTacToe extends JFrame implements ActionListener {

    private JButton[] buttons = new JButton[9];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton restartButton;

    private String xPlayerName;
    private String oPlayerName;
    // The score is kept across rounds; restarting clears only the board.
    private char currentPlayer = 'X';
    private int xScore = 0;
    private int oScore = 0;
    private int moves = 0;
    private boolean gameOver = false;

    public TicTacToe() {
        xPlayerName = askForPlayerName("Player 1", "Player X");
        oPlayerName = askForPlayerName("Player 2", "Player O");

        setTitle("Tic-Tac-Toe");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        add(title, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(3, 3, 5, 5));
        board.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        board.setBackground(Color.DARK_GRAY);

        // Create the nine cells and connect each one to the shared handler.
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 48));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            board.add(buttons[i]);
        }

        add(board, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        statusLabel = new JLabel(getPlayerName(currentPlayer) + "'s turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        scoreLabel = new JLabel("X: 0    |    O: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> restartGame());

        bottomPanel.add(statusLabel);
        bottomPanel.add(scoreLabel);
        bottomPanel.add(restartButton);

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15));

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Ignore clicks after the round ends and clicks on occupied cells.
        if (gameOver) {
            return;
        }

        JButton clickedButton = (JButton) e.getSource();

        if (!clickedButton.getText().isEmpty()) {
            return;
        }

        clickedButton.setText(String.valueOf(currentPlayer));
        moves++;

        if (checkWinner()) {
            gameOver = true;

            if (currentPlayer == 'X') {
                xScore++;
            } else {
                oScore++;
            }

            statusLabel.setText(getPlayerName(currentPlayer) + " wins!");
            scoreLabel.setText("X: " + xScore + "    |    O: " + oScore);

            JOptionPane.showMessageDialog(
                    this, getPlayerName(currentPlayer) + " wins!");

        } else if (moves == 9) {
            gameOver = true;
            statusLabel.setText("It's a draw!");

            JOptionPane.showMessageDialog(this, "The game is a draw!");

        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText(getPlayerName(currentPlayer) + "'s turn (" + currentPlayer + ")");
        }
    }

    private boolean checkWinner() {
        // Each triple describes one row, column, or diagonal on the board.
        int[][] winningCombinations = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        for (int[] combination : winningCombinations) {
            int a = combination[0];
            int b = combination[1];
            int c = combination[2];

            String mark = String.valueOf(currentPlayer);

            if (buttons[a].getText().equals(mark)
                    && buttons[b].getText().equals(mark)
                    && buttons[c].getText().equals(mark)) {
                return true;
            }
        }

        return false;
    }

    private void restartGame() {
        // Start a fresh round while preserving the match score.
        for (JButton button : buttons) {
            button.setText("");
        }

        currentPlayer = 'X';
        moves = 0;
        gameOver = false;

        statusLabel.setText(getPlayerName(currentPlayer) + "'s turn (X)");
    }

    /** Prompts for a player name and supplies a default for blank or cancelled input. */
    private String askForPlayerName(String prompt, String defaultName) {
        String name = JOptionPane.showInputDialog(this, prompt + " name:");
        if (name == null || name.trim().isEmpty()) {
            return defaultName;
        }
        return name.trim();
    }

    /** Returns the display name associated with the current mark. */
    private String getPlayerName(char mark) {
        return mark == 'X' ? xPlayerName : oPlayerName;
    }

    public static void main(String[] args) {
        // Swing components should be created on the event dispatch thread.
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
