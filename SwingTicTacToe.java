import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingTicTacToe extends JFrame implements ActionListener {
    JButton[][] buttons = new JButton[3][3];
    String currentPlayer = "X";

    public SwingTicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Arial", Font.BOLD, 40);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;

        btn.setText(currentPlayer);
        if (checkWinner()) {
            JOptionPane.showMessageDialog(this, currentPlayer + " wins!");
            disableBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
        } else {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            // rows
            if (!buttons[i][0].getText().equals("") &&
                buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                buttons[i][1].getText().equals(buttons[i][2].getText()))
                return true;
            // columns
            if (!buttons[0][i].getText().equals("") &&
                buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                buttons[1][i].getText().equals(buttons[2][i].getText()))
                return true;
        }

        // diagonals
        if (!buttons[0][0].getText().equals("") &&
            buttons[0][0].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][2].getText()))
            return true;

        if (!buttons[0][2].getText().equals("") &&
            buttons[0][2].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][0].getText()))
            return true;

        return false;
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                if (btn.getText().equals("")) return false;
            }
        }
        return true;
    }

    private void disableBoard() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        new SwingTicTacToe();
    }
}
