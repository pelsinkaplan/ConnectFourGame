import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static int rowTileWillState = 0;
    private static Tile[][] Board = new Tile[6][7];
    private static Scanner input = new Scanner(System.in);
    private static String playerColor;

    public static void main(String[] args) {

        System.out.println("You should select players for Connect Four Game. \n H : Human Player \n A : AI Player\n");
        System.out.println("Please select first player. (H/A)");
        String player1 = input.nextLine();
        System.out.println("Please select second player. (H/A)");
        String player2 = input.nextLine();
        printBoard();
        boolean statement = true;
        while (statement) {
            if (player1.equals("H")) {
                humanPlayer(0);
            } else if (player1.equals("A")) {
                aiPlayer(0);
            }
            if (isThereAnyWinner()) {
                statement = false;
                System.out.println("Game finished. Player 1 won the game.");
                continue;
            }
            if (player2.equals("H")) {
                humanPlayer(1);
            } else if (player2.equals("A")) {
                aiPlayer(1);
            }
            if (isThereAnyWinner()) {
                statement = false;
                System.out.println("Game finished. Player 2 won the game.");
            }
            if (isBoardFull()) {
                statement = false;
                System.out.println("Game finished. There is no winner!");
            }
        }
        System.exit(1);

    }

    public static void humanPlayer(int color) {
        if (color == 0)
            playerColor = "X";
        else if (color == 1)
            playerColor = "O";
        System.out.println("It's your turn : " + playerColor + "\nPlease select a column that you want to insert your tile.");
        while (true) {
            int columnSelected = input.nextInt();
            if (canInsertThisIndex(columnSelected - 1)) {
                insertTile(columnSelected - 1, color);
                printBoard();
                break;
            } else {
                System.out.println("This column is not available. Please enter other value!");
            }
        }
    }

    public static void aiPlayer(int color) {
        if (color == 0)
            playerColor = "X";
        else if (color == 1)
            playerColor = "O";
        System.out.println("It's turn of " + playerColor + "\nPlease wait.");
        ArrayList<Integer> values = getChildrenMinimaxValues(getChildren(Board, color), color);
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < values.size(); i++) {
            if (min > values.get(i)) {
                min = values.get(i);
                index = i;
            }
        }
        Board = copyArray(getChildren(Board, color).get(index));
        printBoard();
    }

    public static int minimax(Tile[][] board, int depth, boolean maxPlayer, int color) {
        int value;
        if (depth == 0 || getChildren(board, color) == null)
            return heuristic(board);

        if (maxPlayer) {
            value = Integer.MIN_VALUE;
            ArrayList<Tile[][]> childrenList = getChildren(board, color);
            for (Tile[][] child : childrenList) {
                value = Math.max(value, minimax(child, depth - 1, false, 1));
            }
            return value;
        } else {
            value = Integer.MAX_VALUE;
            ArrayList<Tile[][]> childrenList = getChildren(board, color);
            for (Tile[][] child : childrenList) {
                value = Math.min(value, minimax(child, depth - 1, true, 0));
            }
            return value;
        }
    }

    public static ArrayList<Tile[][]> getChildren(Tile[][] board, int color) {
        ArrayList<Tile[][]> childrenList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (canInsertThisIndex(i)) {
                Tile[][] childBoard = copyArray(board);
                Tile tile = new Tile(rowTileWillState, i, color);
                childBoard[rowTileWillState][i] = tile;
                childrenList.add(childBoard);
            }
        }
        return childrenList;
    }

    public static ArrayList<Integer> getChildrenMinimaxValues(ArrayList<Tile[][]> childrenList, int color) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Tile[][] child : childrenList) {
            values.add(minimax(child, 5, true, color));
        }
        return values;
    }

    public static int heuristic(Tile[][] board) {
        int columnCounter = 0;
        int rowCounter = 0;
        int diagonalRightCounter = 0;
        int maxCounter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == null)
                    continue;
                rowCounter++;
                if (i == j)
                    diagonalRightCounter++;
            }
            if (maxCounter < rowCounter)
                maxCounter = rowCounter;
            if ((maxCounter < diagonalRightCounter))
                maxCounter = diagonalRightCounter;
            rowCounter = 0;
        }

        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                if (board[i][j] == null)
                    continue;
                columnCounter++;
            }
            if (maxCounter < columnCounter)
                maxCounter = columnCounter;
            columnCounter = 0;
        }


        return maxCounter;
    }

    public static Tile[][] copyArray(Tile[][] array) {
        if (array == null) {
            return null;
        }

        final Tile[][] result = new Tile[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return result;
    }

    public static boolean isBoardFull() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (Board[i][j] == null)
                    return false;
            }
        }
        return true;
    }

    public static boolean insertTile(int columnSelected, int tileColor) {
        if (canInsertThisIndex(columnSelected)) {
            Tile tile = new Tile(rowTileWillState, columnSelected, tileColor);
            Board[rowTileWillState][columnSelected] = tile;
            return true;
        } else {
            System.out.println("Column is filled up!");
            return false;
        }
    }

    public static boolean canInsertThisIndex(int columnSelected) {
        for (int i = 5; i >= 0; i--) {
            if (Board[i][columnSelected] == null) {
                rowTileWillState = i;
                return true;
            }
        }

        return false;
    }

    public static boolean isThereAnyWinner() {
        if (verticalWin())
            return true;
        if (horizontalWin())
            return true;
        if (diagonalRightWin())
            return true;
        if (diagonalLeftWin())
            return true;
        return false;
    }

    public static boolean horizontalWin() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (Board[i][j] == null || Board[i][j + 1] == null || Board[i][j + 2] == null || Board[i][j + 3] == null)
                    continue;
                int color = Board[i][j].tileColor;
                if (Board[i][j + 1].tileColor == color && Board[i][j + 2].tileColor == color && Board[i][j + 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean verticalWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (Board[i][j] == null || Board[i + 1][j] == null || Board[i + 2][j] == null || Board[i + 3][j] == null)
                    continue;
                int color = Board[i][j].tileColor;
                if (Board[i + 1][j].tileColor == color && Board[i + 2][j].tileColor == color && Board[i + 3][j].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean diagonalRightWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (Board[i][j] == null || Board[i + 1][j + 1] == null || Board[i + 2][j + 2] == null || Board[i + 3][j + 3] == null)
                    continue;
                int color = Board[i][j].tileColor;
                if (Board[i + 1][j + 1].tileColor == color && Board[i + 2][j + 2].tileColor == color && Board[i + 3][j + 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean diagonalLeftWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 2; j--) {
                if (Board[i][j] == null || Board[i + 1][j - 1] == null || Board[i + 2][j - 2] == null || Board[i + 3][j - 3] == null)
                    continue;
                int color = Board[i][j].tileColor;
                if (Board[i + 1][j -
                        1].tileColor == color && Board[i + 2][j - 2].tileColor == color && Board[i + 3][j - 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printBoard() {
        System.out.println("-----------------------------");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (Board[i][j] == null)
                    System.out.print("|   ");
                else {
                    if (Board[i][j].tileColor == 0)
                        playerColor = "X";
                    else if (Board[i][j].tileColor == 1)
                        playerColor = "O";
                    System.out.print("| " + playerColor + " ");
                }
                if (j == 6)
                    System.out.print("|");

            }
            System.out.println("\n-----------------------------");
        }
        System.out.println("  1   2   3   4   5   6   7");
    }

    static class Tile {
        private int locaitonX;
        private int locaitonY;
        private int tileColor;

        public Tile(int locaitonX, int locaitonY, int tileColor) {
            this.locaitonX = locaitonX;
            this.locaitonY = locaitonY;
            this.tileColor = tileColor;
        }

        public int getLocaitonX() {
            return locaitonX;
        }

        public int getLocaitonY() {
            return locaitonY;
        }

        public int getTileColor() {
            return tileColor;
        }
    }
}
