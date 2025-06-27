import java.util.*;

// ----------- State Design Pattern Interfaces & Classes --------------
interface GameState {
    void play(Game game);
}

class PlayerTurnState implements GameState {
    @Override
    public void play(Game game) {
        Player current = game.getCurrentPlayer();
        System.out.println(current.getName() + " (" + current.getSymbol() + "), choose a slot (1-9): ");
        int slot = current.getMove(game.getBoard());

        if (slot == -1) {
            System.out.println("Invalid move. Try again.");
            return;
        }

        game.getBoard().placeMark(slot, current.getSymbol());
        game.setState(new CheckWinnerState());
    }
}

class CheckWinnerState implements GameState {
    @Override
    public void play(Game game) {
        game.getBoard().print();

        String result = game.getBoard().checkWinner();
        if (result == null) {
            game.switchPlayer();
            game.setState(new PlayerTurnState());
        } else {
            game.setWinner(result);
            game.setState(new GameOverState());
        }
    }
}

class GameOverState implements GameState {
    @Override
    public void play(Game game) {
        if (game.getWinner().equalsIgnoreCase("draw")) {
            System.out.println("It's a draw! Thanks for playing.");
        } else {
            System.out.println("Congratulations! " + game.getCurrentPlayer().getName() +
                " (" + game.getWinner() + ") has won!");
        }
        game.setFinished(true);
    }
}

// ----------- Factory Design Pattern for Player Creation --------------
abstract class Player {
    protected String name;
    protected String symbol;

    public Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public abstract int getMove(Board board);

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}

class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name, String symbol, Scanner scanner) {
        super(name, symbol);
        this.scanner = scanner;
    }

    @Override
    public int getMove(Board board) {
        try {
            int move = scanner.nextInt();
            if (move < 1 || move > 9 || !board.isSlotAvailable(move - 1)) {
                return -1;
            }
            return move - 1;
        } catch (InputMismatchException e) {
            scanner.next(); // clear input
            return -1;
        }
    }
}

class PlayerFactory {
    public static Player createPlayer(String type, String name, String symbol, Scanner scanner) {
        if (type.equalsIgnoreCase("human")) {
            return new HumanPlayer(name, symbol, scanner);
        }
        // Can be extended for ComputerPlayer later
        return null;
    }
}

// ------------------- Board Class -----------------------
class Board {
    private String[] board;

    public Board() {
        board = new String[9];
        for (int i = 0; i < 9; i++) {
            board[i] = String.valueOf(i + 1);
        }
    }

    public boolean isSlotAvailable(int index) {
        return board[index].equals(String.valueOf(index + 1));
    }

    public void placeMark(int index, String mark) {
        board[index] = mark;
    }

    public void print() {
        System.out.println("|---|---|---|");
        for (int i = 0; i < 9; i += 3) {
            System.out.println("| " + board[i] + " | " + board[i + 1] + " | " + board[i + 2] + " |");
            if (i < 6) System.out.println("|-----------|");
        }
        System.out.println("|---|---|---|");
    }

    public String checkWinner() {
        String[][] combos = {
            {board[0], board[1], board[2]},
            {board[3], board[4], board[5]},
            {board[6], board[7], board[8]},
            {board[0], board[3], board[6]},
            {board[1], board[4], board[7]},
            {board[2], board[5], board[8]},
            {board[0], board[4], board[8]},
            {board[2], board[4], board[6]},
        };

        for (String[] line : combos) {
            if (line[0].equals(line[1]) && line[1].equals(line[2])) {
                return line[0]; // X or O
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i].equals(String.valueOf(i + 1))) {
                return null; // still playing
            }
        }
        return "draw";
    }
}

// ----------------- Game Class (Context) -------------------
class Game {
    private GameState state;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    private boolean isFinished = false;
    private String winner;

    public Game(Player p1, Player p2) {
        this.board = new Board();
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;
        this.state = new PlayerTurnState();
    }

    public void start() {
        System.out.println("Welcome to Tic Tac Toe!");
        board.print();
        while (!isFinished) {
            state.play(this);
        }
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }
}

// ---------------- Main Class -------------------
public class Geeks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player p1 = PlayerFactory.createPlayer("human", "Player 1", "X", scanner);
        Player p2 = PlayerFactory.createPlayer("human", "Player 2", "O", scanner);

        Game game = new Game(p1, p2);
        game.start();

        scanner.close();
    }
}
