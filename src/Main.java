import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static int rowTileWillState = 0;
    private static Tile[][] Board = new Tile[6][7];
    private static Scanner input = new Scanner(System.in);
    private static String playerColor;

    public static void main(String[] args) {
        int depth = 0;
        int heuristic = 0;
        int depth2 = 0;
        int heuristic2 = 0;
        System.out.println("You should select players for Connect Four Game. \n H : Human Player \n A : AI Player\n");
        System.out.println("Please select first player. (H/A)");
        String player1 = input.nextLine();
        System.out.println("Please select second player. (H/A)");
        String player2 = input.nextLine();
        if (player1.equals("A")) {
            System.out.println("Please enter a depth for first player : ");
            depth = input.nextInt();
            System.out.println("Please select a heuristic for first player (1-3) : ");
            heuristic = input.nextInt();
        }

        if (player2.equals("A")) {
            System.out.println("Please enter a depth for second player : ");
            depth2 = input.nextInt();
            System.out.println("Please select a heuristic for second player (1-3) : ");
            heuristic2 = input.nextInt();
        }
        printBoard();
        boolean statement = true;
        while (statement) {
            if (player1.equals("H")) {
                humanPlayer(0);
            } else if (player1.equals("A")) {
                aiPlayer(0, depth, heuristic);
            }
            if (isThereAnyWinner(Board)) {
                statement = false;
                System.out.println("Game finished. Player 1 won the game.");
                continue;
            }
            if (player2.equals("H")) {
                humanPlayer(1);
            } else if (player2.equals("A")) {
                aiPlayer(1, depth2, heuristic2);
            }
            if (isThereAnyWinner(Board)) {
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
            if (canInsertThisIndex(columnSelected - 1, Board)) {
                insertTile(columnSelected - 1, color, Board);
                printBoard();
                break;
            } else {
                System.out.println("This column is not available. Please enter other value!");
            }
        }
    }

    public static void aiPlayer(int color, int depth, int heuristic) {
        if (color == 0)
            playerColor = "X";
        else if (color == 1)
            playerColor = "O";
        System.out.println("It's turn of " + playerColor + "\nPlease wait.");
        ArrayList<Integer> values = getChildrenMinimaxValues(getChildren(Board, color), color, depth, heuristic);
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < values.size(); i++) {
            if (max < values.get(i)) {
                max = values.get(i);
                index = i;
            }
        }
        Board = copyArray(getChildren(Board, color).get(index));
        printBoard();
    }

    public static int minimax(Tile[][] board, int depth, boolean maxPlayer, int color, int heuristic) {
        int value;
        int h;


        if (depth == 0 || getChildren(board, color) == null || isThereAnyWinner(board)) {
            if (heuristic == 1)
                return heuristic(board, color);
            else if (heuristic == 2)
                return heuristic2(board, color);
            else if (heuristic == 3)
                return heuristic3(board, color);
            else
                System.exit(1);
        }


        if (maxPlayer) {
            value = Integer.MIN_VALUE;
            ArrayList<Tile[][]> childrenList = getChildren(board, 0);
            for (Tile[][] child : childrenList) {
                value = Math.max(value, minimax(child, depth - 1, false, 1, heuristic));
            }
            return value;
        } else {
            value = Integer.MAX_VALUE;
            ArrayList<Tile[][]> childrenList = getChildren(board, 1);
            for (Tile[][] child : childrenList) {
                value = Math.min(value, minimax(child, depth - 1, true, 0, heuristic));
            }
            return value;
        }
    }

    public static ArrayList<Tile[][]> getChildren(Tile[][] board, int color) {
        ArrayList<Tile[][]> childrenList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (canInsertThisIndex(i, board)) {
                Tile[][] childBoard = copyArray(board);
                insertTile(i, color, childBoard);
                childrenList.add(childBoard);
            }
        }
        return childrenList;
    }

    public static ArrayList<Integer> getChildrenMinimaxValues(ArrayList<Tile[][]> childrenList, int color, int depth, int heuristic) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Tile[][] child : childrenList) {
            int value = minimax(child, depth, true, color, heuristic);
            System.out.println(value);
            values.add(value);
        }
        return values;
    }


    public static int heuristic(Tile[][] board, int turn) {
        int maxCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != null && board[i][j + 1] != null && board[i][j + 2] != null && board[i][j + 3] != null) {
                    int color = board[i][j].tileColor;
                    if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && board[i][j + 3].tileColor == color && color != turn) {
                        maxCounter = -100000;
                        break;
                    }
                }
                if (maxCounter != -100000) {
                    int counter4 = horizontalFourRow(board, turn);
                    int counter3 = horizontalThreeRow(board, turn);
                    int counter2 = horizontalTwoRow(board, turn);
                    counter4 += verticalFourColumn(board, turn);
                    counter3 += verticalThreeColumn(board, turn);
                    counter2 += verticalTwoColumn(board, turn);
                    counter4 += diagonalRightFour(board, turn);
                    counter3 += diagonalRightThree(board, turn);
                    counter2 += diagonalRightTwo(board, turn);
                    counter4 += diagonalLeftFour(board, turn);
                    counter3 += diagonalLeftThree(board, turn);
                    counter2 += diagonalLeftTwo(board, turn);
                    maxCounter += 100000 * Math.pow(counter4, counter4) + 100 * Math.pow(counter3, counter3) + Math.pow(counter2, counter2);
                }
            }
        }
        return maxCounter;
    }

    public static int heuristic2(Tile[][] board, int turn) {
        int maxCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != null && board[i][j + 1] != null && board[i][j + 2] != null && board[i][j + 3] != null) {
                    int color = board[i][j].tileColor;
                    if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && board[i][j + 3].tileColor == color && color != turn) {
                        maxCounter = -100000;
                        break;
                    }
                }
                if (maxCounter != -100000) {
                    int counter3 = horizontalThreeRow(board, turn);
                    int counter2 = horizontalTwoRow(board, turn);
                    counter3 += verticalThreeColumn(board, turn);
                    counter2 += verticalTwoColumn(board, turn);
                    counter3 += diagonalRightThree(board, turn);
                    counter2 += diagonalRightTwo(board, turn);
                    counter3 += diagonalLeftThree(board, turn);
                    counter2 += diagonalLeftTwo(board, turn);
                    maxCounter += 100 * Math.pow(counter3, counter3) + Math.pow(counter2, counter2);
                }
            }
        }
        return maxCounter;
    }

    public static int heuristic3(Tile[][] board, int turn) {
        int maxCounter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != null && board[i][j + 1] != null && board[i][j + 2] != null && board[i][j + 3] != null) {
                    int color = board[i][j].tileColor;
                    if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && board[i][j + 3].tileColor == color && color != turn) {
                        maxCounter = -100000;
                        break;
                    }
                }
                if (maxCounter != -100000) {
                    int counter2 = horizontalTwoRow(board, turn);
                    counter2 += verticalTwoColumn(board, turn);
                    counter2 += diagonalRightTwo(board, turn);
                    counter2 += diagonalLeftTwo(board, turn);
                    maxCounter += Math.pow(counter2, counter2);
                }
            }
        }
        return maxCounter;
    }

    /**
     * Check methods for heuristic
     **/

    public static int horizontalFourRow(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null || board[i][j + 1] == null || board[i][j + 2] == null || board[i][j + 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && board[i][j + 3].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int horizontalThreeRow(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == null || board[i][j + 1] == null || board[i][j + 2] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int horizontalTwoRow(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == null || board[i][j + 1] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i][j + 1].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int verticalFourColumn(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == null || board[i + 1][j] == null || board[i + 2][j] == null || board[i + 3][j] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j].tileColor == color && board[i + 2][j].tileColor == color && board[i + 3][j].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int verticalThreeColumn(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == null || board[i + 1][j] == null || board[i + 2][j] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j].tileColor == color && board[i + 2][j].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int verticalTwoColumn(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == null || board[i + 1][j] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalRightFour(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null || board[i + 1][j + 1] == null || board[i + 2][j + 2] == null || board[i + 3][j + 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j + 1].tileColor == color && board[i + 2][j + 2].tileColor == color && board[i + 3][j + 3].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalRightThree(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == null || board[i + 1][j + 1] == null || board[i + 2][j + 2] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j + 1].tileColor == color && board[i + 2][j + 2].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalRightTwo(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] == null || board[i + 1][j + 1] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j + 1].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalLeftFour(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 2; j--) {
                if (board[i][j] == null || board[i + 1][j - 1] == null || board[i + 2][j - 2] == null || board[i + 3][j - 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j - 1].tileColor == color && board[i + 2][j - 2].tileColor == color && board[i + 3][j - 3].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalLeftThree(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 1; j--) {
                if (board[i][j] == null || board[i + 1][j - 1] == null || board[i + 2][j - 2] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j - 1].tileColor == color && board[i + 2][j - 2].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int diagonalLeftTwo(Tile[][] board, int turn) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 0; j--) {
                if (board[i][j] == null || board[i + 1][j - 1] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j - 1].tileColor == color && color == turn) {
                    counter++;
                }
            }
        }
        return counter;
    }


    /**
     * Other methods for game
     **/


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

    public static boolean isBoardFull() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (Board[i][j] == null)
                    return false;
            }
        }
        return true;
    }

    public static boolean insertTile(int columnSelected, int tileColor, Tile[][] board) {
        if (canInsertThisIndex(columnSelected, board)) {
            Tile tile = new Tile(rowTileWillState, columnSelected, tileColor);
            board[rowTileWillState][columnSelected] = tile;
            return true;
        }
        return false;
    }

    public static boolean canInsertThisIndex(int columnSelected, Tile[][] board) {
        for (int i = 5; i >= 0; i--) {
            if (board[i][columnSelected] == null) {
                rowTileWillState = i;
                return true;
            }
        }

        return false;
    }

    public static boolean isThereAnyWinner(Tile[][] board) {
        if (verticalWin(board))
            return true;
        if (horizontalWin(board))
            return true;
        if (diagonalRightWin(board))
            return true;
        if (diagonalLeftWin(board))
            return true;
        return false;
    }


    /**
     * Check methods to find the winner
     **/

    public static boolean horizontalWin(Tile[][] board) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null || board[i][j + 1] == null || board[i][j + 2] == null || board[i][j + 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i][j + 1].tileColor == color && board[i][j + 2].tileColor == color && board[i][j + 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean verticalWin(Tile[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == null || board[i + 1][j] == null || board[i + 2][j] == null || board[i + 3][j] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j].tileColor == color && board[i + 2][j].tileColor == color && board[i + 3][j].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean diagonalRightWin(Tile[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null || board[i + 1][j + 1] == null || board[i + 2][j + 2] == null || board[i + 3][j + 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j + 1].tileColor == color && board[i + 2][j + 2].tileColor == color && board[i + 3][j + 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean diagonalLeftWin(Tile[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 6; j > 2; j--) {
                if (board[i][j] == null || board[i + 1][j - 1] == null || board[i + 2][j - 2] == null || board[i + 3][j - 3] == null)
                    continue;
                int color = board[i][j].tileColor;
                if (board[i + 1][j -
                        1].tileColor == color && board[i + 2][j - 2].tileColor == color && board[i + 3][j - 3].tileColor == color) {
                    return true;
                }
            }
        }
        return false;
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
