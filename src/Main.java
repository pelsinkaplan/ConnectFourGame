import java.util.Scanner;

public class Main {

    private static int rowTileWillState = 0;
    private static Tile[][] Board = new Tile[6][7];
    private static Scanner input = new Scanner(System.in);
    private static String playerColor;
    private static int winner;

    public static void main(String[] args) {

        System.out.println("You should select players for Connect Four Game. \n H : Human Player \n A : AI Player\n");
        System.out.println("Please select first player. (H/A)");
        String player1 = input.nextLine();
        System.out.println("Please select second player. (H/A)");
        String player2 = input.nextLine();

        boolean statement = true;
        while (statement) {
            if (player1.equals("H")) {
                humanPlayer(0);
            } else if (player1.equals("A")) {
                //ai implmentasyonundan sonra eklenecek
            }
            if (isThereAnyWinner()) {
                statement = false;
                System.out.println("Game finished. Player 1 won the game.");
                continue;
            }
            if (player2.equals("H")) {
                humanPlayer(1);
            } else if (player2.equals("A")) {
                //ai implmentasyonundan sonra eklenecek
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
        printBoard();
        System.out.println("It's your turn : " + playerColor + "\nPlease select a column that you want to insert your tile.");
        while (true) {
            int columnSelected = input.nextInt();
            if (canInsertThisIndex(columnSelected)) {
                insertTile(columnSelected, color);
                break;
            } else {
                System.out.println("This column is not available. Please enter other value!");
            }
        }
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
        System.out.println("-------------------------");
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

            }
            System.out.println("\n-------------------------");
        }
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
