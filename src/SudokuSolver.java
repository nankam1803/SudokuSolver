import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class SudokuSolver {
    private static final int SIZE = 9;
    private final JTextField[][] cells = new JTextField[SIZE][SIZE];
    private final JFrame frame = new JFrame("Sudoku Solver");

    public SudokuSolver() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                cells[row][col].setPreferredSize(new Dimension(50, 50));

                int finalRow = row;
                int finalCol = col;
                cells[row][col].addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        char c = evt.getKeyChar();
                        if (!(Character.isDigit(c) && c != '0') || cells[finalRow][finalCol].getText().length() > 0) {
                            evt.consume(); // Reject invalid input
                        }
                    }
                });

                // Add grid styling
                if ((row / 3 + col / 3) % 2 == 0) {
                    cells[row][col].setBackground(new Color(220, 220, 220));
                }

                boardPanel.add(cells[row][col]);
            }
        }

        JPanel buttonPanel = new JPanel();
        JButton solveButton = new JButton("Solve Sudoku");
        JButton resetButton = new JButton("Reset");
        JButton generateButton = new JButton("Generate Puzzle");

        solveButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        generateButton.setFont(new Font("Arial", Font.BOLD, 16));

        solveButton.addActionListener(e -> solveSudoku());
        resetButton.addActionListener(e -> resetBoard());
        generateButton.addActionListener(e -> generatePuzzle());

        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(generateButton);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void solveSudoku() {
        int[][] board = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText();
                if (!text.isEmpty() && text.matches("[1-9]")) {
                    board[row][col] = Integer.parseInt(text);
                } else {
                    board[row][col] = 0;
                }
            }
        }

        if (solve(board)) {
            updateBoard(board);
        } else {
            JOptionPane.showMessageDialog(frame, "No solution found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBoard(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = 0; 
                        }
                    }
                    return false; 
                }
            }
        }
        return true; 
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int subGridRow = (row / 3) * 3;
        int subGridCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[subGridRow + i][subGridCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
            }
        }
    }

    private void generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];

        solve(board);

        Random rand = new Random();
        for (int i = 0; i < 40; i++) { 
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            board[row][col] = 0;
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    cells[row][col].setEditable(false);
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
