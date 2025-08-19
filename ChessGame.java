import java.util.Scanner;

// Abstract class for chess pieces
abstract class Piece {
    protected boolean isWhite;
    protected int row, col;

    public Piece(boolean isWhite, int row, int col) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract boolean isValidMove(int newRow, int newCol, Piece[][] board);
}

// Concrete classes for each piece type
class Pawn extends Piece {
    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        // Single move forward
        if (newCol == col && newRow == row + direction && board[newRow][newCol] == null) {
            return true;
        }
        // Double move from starting position
        if (newCol == col && row == startRow && newRow == row + 2 * direction
                && board[newRow][newCol] == null && board[row + direction][col] == null) {
            return true;
        }
        // Capture diagonally
        if (Math.abs(newCol - col) == 1 && newRow == row + direction
                && board[newRow][newCol] != null && board[newRow][newCol].isWhite != isWhite) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return isWhite ? "P" : "p";
    }
}

class Rook extends Piece {
    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        if (newRow != row && newCol != col) return false;

        int start = newRow != row ? Math.min(row, newRow) + 1 : Math.min(col, newCol) + 1;
        int end = newRow != row ? Math.max(row, newRow) : Math.max(col, newCol);
        int fixed = newRow != row ? newCol : newRow;

        for (int i = start; i < end; i++) {
            if (newRow != row && board[i][fixed] != null) return false;
            if (newCol != col && board[fixed][i] != null) return false;
        }
        return board[newRow][newCol] == null || board[newRow][newCol].isWhite != isWhite;
    }

    @Override
    public String toString() {
        return isWhite ? "R" : "r";
    }
}

class Knight extends Piece {
    public Knight(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            return board[newRow][newCol] == null || board[newRow][newCol].isWhite != isWhite;
        }
        return false;
    }

    @Override
    public String toString() {
        return isWhite ? "N" : "n";
    }
}

class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        if (Math.abs(newRow - row) != Math.abs(newCol - col)) return false;

        int rowDir = newRow > row ? 1 : -1;
        int colDir = newCol > col ? 1 : -1;
        int steps = Math.abs(newRow - row);

        for (int i = 1; i < steps; i++) {
            if (board[row + i * rowDir][col + i * colDir] != null) return false;
        }
        return board[newRow][newCol] == null || board[newRow][newCol].isWhite != isWhite;
    }

    @Override
    public String toString() {
        return isWhite ? "B" : "b";
    }
}

class Queen extends Piece {
    public Queen(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        Rook rook = new Rook(isWhite, row, col);
        Bishop bishop = new Bishop(isWhite, row, col);
        return rook.isValidMove(newRow, newCol, board) || bishop.isValidMove(newRow, newCol, board);
    }

    @Override
    public String toString() {
        return isWhite ? "Q" : "q";
    }
}

class King extends Piece {
    public King(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        if (rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0)) {
            return board[newRow][newCol] == null || board[newRow][newCol].isWhite != isWhite;
        }
        return false;
    }

    @Override
    public String toString() {
        return isWhite ? "K" : "k";
    }
}

// Main game class
public class ChessGame {
    private Piece[][] board;
    private boolean whiteTurn;
    private Scanner scanner;

    public ChessGame() {
        board = new Piece[8][8];
        whiteTurn = true;
        scanner = new Scanner(System.in);
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize pawns
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(false, 1, col); // Black pawns
            board[6][col] = new Pawn(true, 6, col);  // White pawns
        }
        // Initialize other pieces
        board[0][0] = new Rook(false, 0, 0);
        board[0][7] = new Rook(false, 0, 7);
        board[7][0] = new Rook(true, 7, 0);
        board[7][7] = new Rook(true, 7, 7);

        board[0][1] = new Knight(false, 0, 1);
        board[0][6] = new Knight(false, 0, 6);
        board[7][1] = new Knight(true, 7, 1);
        board[7][6] = new Knight(true, 7, 6);

        board[0][2] = new Bishop(false, 0, 2);
        board[0][5] = new Bishop(false, 0, 5);
        board[7][2] = new Bishop(true, 7, 2);
        board[7][5] = new Bishop(true, 7, 5);

        board[0][3] = new Queen(false, 0, 3);
        board[7][3] = new Queen(true, 7, 3);

        board[0][4] = new King(false, 0, 4);
        board[7][4] = new King(true, 7, 4);
    }

    private void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                System.out.print(piece == null ? ". " : piece.toString() + " ");
            }
            System.out.println(8 - row);
        }
        System.out.println("  a b c d e f g h");
    }

    private boolean makeMove() {
        System.out.println((whiteTurn ? "White" : "Black") + "'s turn. Enter move (e.g., e2 e4): ");
        String input = scanner.nextLine().trim();
        if (input.equals("quit")) return false;

        String[] parts = input.split(" ");
        if (parts.length != 2) {
            System.out.println("Invalid input. Use format: e2 e4");
            return true;
        }

        int[] start = parsePosition(parts[0]);
        int[] end = parsePosition(parts[1]);
        if (start == null || end == null) {
            System.out.println("Invalid position.");
            return true;
        }

        int startRow = start[0], startCol = start[1];
        int endRow = end[0], endCol = end[1];

        Piece piece = board[startRow][startCol];
        if (piece == null || piece.isWhite() != whiteTurn) {
            System.out.println("Invalid piece selection.");
            return true;
        }

        if (piece.isValidMove(endRow, endCol, board)) {
            board[endRow][endCol] = piece;
            board[startRow][startCol] = null;
            piece.row = endRow;
            piece.col = endCol;
            whiteTurn = !whiteTurn;
        } else {
            System.out.println("Invalid move.");
        }
        return true;
    }

    private int[] parsePosition(String pos) {
        if (pos.length() != 2) return null;
        int col = pos.charAt(0) - 'a';
        int row = 8 - (pos.charAt(1) - '0');
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
        return new int[]{row, col};
    }

    public void play() {
        while (true) {
            printBoard();
            if (!makeMove()) break;
        }
        scanner.close();
        System.out.println("Game ended.");
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        game.play();
    }
}