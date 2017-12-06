import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class NQueens {

    private static int maxIterations;
    private static Random random = new Random();
    private static int iterations = 0;

    private static ChessBoard moveQueen(ChessBoard chessBoard, int col) {

        int bestRow = chessBoard.getRowWithLeastCollisions(col);
//        int q = chessBoard.queens[col];
//        chessBoard = updateRow(chessBoard, q, bestRow, col);
//        chessBoard = updateFirstDiagonal(chessBoard, chessBoard.queenBoardIndex(col), bestRow * chessBoard.queens.length + col);
//        chessBoard = updateSecondDiagonal(chessBoard, chessBoard.queenBoardIndex(col), bestRow * chessBoard.queens.length + col);
        chessBoard.queens[col] = bestRow;
        return chessBoard;
    }

    private static ChessBoard updateFirstDiagonal(ChessBoard chessBoard, int fromPos, int toPos) {
        int fromCol = fromPos % chessBoard.queens.length - 1;
        int fromRow = fromPos / chessBoard.queens.length - 1;
        while (fromRow >= 0 && fromCol >= 0) {
            chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + fromCol]--;
            fromRow--;
            fromCol--;
        }
        fromCol = fromPos % chessBoard.queens.length + 1;
        fromRow = fromPos / chessBoard.queens.length + 1;
        while (fromRow < chessBoard.queens.length && fromCol < chessBoard.queens.length) {
            chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + fromCol]--;
            fromRow++;
            fromCol++;
        }
        int toCol = toPos % chessBoard.queens.length - 1;
        int toRow = toPos / chessBoard.queens.length - 1;
        while (toRow >= 0 && toCol >= 0) {
            chessBoard.conflictsOnPosition[toRow * chessBoard.queens.length + toCol]++;
            toRow--;
            toCol--;
        }
        toCol = toPos % chessBoard.queens.length + 1;
        toRow = toPos / chessBoard.queens.length + 1;
        while (toRow < chessBoard.queens.length && toCol < chessBoard.queens.length) {
            chessBoard.conflictsOnPosition[toRow * chessBoard.queens.length + toCol]++;
            toRow++;
            toCol++;
        }
        return chessBoard;
    }

    private static ChessBoard updateSecondDiagonal(ChessBoard chessBoard, int fromPos, int toPos) {
        int currentCol = fromPos % chessBoard.queens.length;
        int currentRow = fromPos / chessBoard.queens.length;

        int fromCol = currentCol - 1;
        int fromRow = currentRow + 1;
        while (fromRow < chessBoard.queens.length && fromCol >= 0) {
            chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + fromCol]--;
            fromRow++;
            fromCol--;
        }
        fromCol = currentCol + 1;
        fromRow = currentRow - 1;
        while (fromRow > 0 && fromCol < chessBoard.queens.length) {
            chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + fromCol]--;
            fromRow--;
            fromCol++;
        }
        currentCol = toPos % chessBoard.queens.length;
        currentRow = toPos / chessBoard.queens.length;
        int toCol = currentCol - 1;
        int toRow = currentRow + 1;
        while (toRow < chessBoard.queens.length && toCol >= 0) {
            chessBoard.conflictsOnPosition[toRow * chessBoard.queens.length + toCol]++;
            toRow++;
            toCol--;
        }
        toCol = currentCol + 1;
        toRow = currentRow - 1;
        while (toRow >= 0 && toCol < chessBoard.queens.length) {
            chessBoard.conflictsOnPosition[toRow * chessBoard.queens.length + toCol]++;
            toRow--;
            toCol++;
        }
        return chessBoard;
    }

    private static ChessBoard updateRow(ChessBoard chessBoard, int fromRow, int toRow, int col) {
        for (int i = 0; i < chessBoard.queens.length; i++) {
            if (i != col) {
                chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + i]--;
                chessBoard.conflictsOnPosition[toRow * chessBoard.queens.length + i]++;
            }
        }
        return chessBoard;
    }

    private static String printHistogram(ChessBoard chessBoard) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chessBoard.conflictsOnPosition.length; i++) {
            if (i % chessBoard.queens.length == 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(" " + chessBoard.conflictsOnPosition[i] + " ");
        }
        return stringBuilder.toString();
    }

    private static int[] createHistogram(ChessBoard chessBoard) {
        int len = chessBoard.queens.length;
        int[] histogram = new int[len * len];

        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                histogram[row * len + col] = conflicts(row, col, chessBoard.queens);
            }
        }
//
//        for (int i = 1; i <= histogram.length; i++) {
//            if (i % len == 0) {
//                System.out.println(histogram[i - 1]);
//            } else {
//                System.out.print(histogram[i - 1] + " ");
//            }
//        }
//        System.out.println();
        return histogram;
    }

    private static int conflicts(int row, int col, int[] queens) {
        int count = 0;
        for (int c = 0; c < queens.length; c++) {
            if (c == col) continue;
            int r = queens[c];
            if (r == row || Math.abs(r - row) == Math.abs(c - col)) count++;
        }
        return count;
    }


    private static ChessBoard fillChessBoard(int n) {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.queens = new int[n];
        Random generator = new Random();
        int rowIndex;
        int[] usedRows = new int[n];
        for (int i = 0; i < n; i++) {
            while (true) {
                rowIndex = generator.nextInt(n);
                if (usedRows[rowIndex] == 0) {
                    usedRows[rowIndex] = 1;
                    chessBoard.queens[i] = rowIndex;
                    break;
                }
            }
//            System.out.print(chessBoard.queens[i] + " ");
//            chessBoard.queens[i] = 0;
        }
//        System.out.println();
        return chessBoard;
    }

    private static boolean minConflicts(ChessBoard chessBoard) {
        boolean foundSolution = false;
        while (iterations < maxIterations) {
            iterations++;
            int col = chessBoard.getColumnWithMostCollisions();
            chessBoard = moveQueen(chessBoard, col);
            if (chessBoard.solved()) {
                foundSolution = true;
                break;
            }
        }
        System.out.println(iterations);
        return foundSolution;
    }

    public void solveNQueens(int n) {
        maxIterations = 2 * n;
        boolean foundSolution = false;
        while (!foundSolution) {
            iterations = 0;
            ChessBoard chessBoard = fillChessBoard(n);
//            chessBoard.conflictsOnPosition = createHistogram(chessBoard);
            foundSolution = minConflicts(chessBoard);
        }
    }

    public static void main(String[] args) {
        int n;
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        NQueens nQueens = new NQueens();
        nQueens.solveNQueens(n);
    }

    private static class ChessBoard {
        public int[] queens;
        public int[] conflictsOnPosition;
        private Random random = new Random();

        public int getColumnWithMostCollisions() {
            List<Integer> big = new ArrayList<>();
            big.add(0);
            int largest;
            int maxConflicts = 0;
            for (int col = 0; col < queens.length; col++) {
                largest = conflicts(queens[col], col, queens);
                if (largest > maxConflicts) {
                    big.clear();
                    big.add(col);
                    maxConflicts = largest;
                } else if (largest == maxConflicts) {
                    big.add(col);
                }
            }
            return big.get(random.nextInt(big.size()));
        }

        public int getRowWithLeastCollisions(int col) {
            List<Integer> small = new ArrayList<>();
            int minConflicts = queens.length;
            small.add(col);
            int currentConflicts;

            for (int row = 0; row < queens.length; row++) {
                currentConflicts = conflicts(row, col, queens);
                if (currentConflicts < minConflicts) {
                    small.clear();
                    small.add(row);
                    minConflicts = currentConflicts;
                } else if (currentConflicts == minConflicts) {
                    small.add(row);
                }
            }
            return small.get(random.nextInt(small.size()));
        }

        public int queenBoardIndex(int col) {
            return (queens.length * queens[col] + col);
        }

        public boolean solved() {
            for (int col = 0; col < queens.length; col++) {
                if (conflicts(queens[col], col, queens) != 0) {
                    return false;
                }
            }
//            System.out.println(toString());
            return true;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int row = 0; row < queens.length; row++) {
                for (int col = 0; col < queens.length; col++) {
                    stringBuilder.append(queens[col] == row ? "* " : "- ");
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }

        public int[] getQueens() {
            return queens;
        }

        public void setQueens(int[] queens) {
            this.queens = queens;
        }

        public int[] getConflictsOnPosition() {
            return conflictsOnPosition;
        }

        public void setConflictsOnPosition(int[] conflictsOnPosition) {
            this.conflictsOnPosition = conflictsOnPosition;
        }

    }

}
//import java.io.PrintStream;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class NQueens {
//
//    private static class Board {
//        Random random = new Random();
//
//        /**
//         * The row for each column, For example [3,7,0,4,6,1,5,2] represents
//         * <pre>
//         *     ..Q.....
//         *     .....Q..
//         *     .......Q
//         *     Q.......
//         *     ...Q....
//         *     ......Q.
//         *     ....Q...
//         *     .Q......
//         * </pre>
//         */
//        int[] rows;
//
//        /**
//         * Creates a new n x n board and randomly fills it with one
//         * queen in each column.
//         */
//        Board(int n) {
//            rows = new int[n];
//            scramble();
//        }
//
//        /**
//         * Randomly fills the board with one queen in each column.
//         */
//        void scramble() {
//            for (int i = 0, n = rows.length; i < n; i++) {
//                rows[i] = i;
//            }
//            for (int i = 0, n = rows.length; i < n; i++) {
//                int j = random.nextInt(n);
//                int rowToSwap = rows[i];
//                rows[i] = rows[j];
//                rows[j] = rowToSwap;
//            }
//        }
//
//        /**
//         * Returns the number of queens that conflict with (row,col), not
//         * counting the queen in column col.
//         */
//        int conflicts(int row, int col) {
//            int count = 0;
//            for (int c = 0; c < rows.length; c++) {
//                if (c == col) continue;
//                int r = rows[c];
//                if (r == row || Math.abs(r-row) == Math.abs(c-col)) count++;
//            }
//            return count;
//        }
//
//        /**
//         * Fills the board with a legal arrangement of queens.
//         */
//        void solve() {
//            int moves = 0;
//
//            // This would be a lot faster if we used arrays of ints instead.
//            ArrayList<Integer> candidates = new ArrayList<Integer>();
//
//            while (true) {
//
//                // Find nastiest queen
//                int maxConflicts = 0;
//                candidates.clear();
//                for (int c = 0; c < rows.length; c++) {
//                    int conflicts = conflicts(rows[c], c);
//                    if (conflicts == maxConflicts) {
//                        candidates.add(c);
//                    } else if (conflicts > maxConflicts) {
//                        maxConflicts = conflicts;
//                        candidates.clear();
//                        candidates.add(c);
//                    }
//                }
//
//                if (maxConflicts == 0) {
//                    // Checked *every* queen and found no conflicts
//                    System.out.println("MOVES:::" + moves);
//                    System.exit(1);
//                }
//
//                // Pick a random queen from those that had the most conflicts
//                int worstQueenColumn =
//                        candidates.get(random.nextInt(candidates.size()));
//
//                // Move her to the place with the least conflicts.
//                int minConflicts = rows.length;
//                candidates.clear();
//                for (int r = 0; r < rows.length; r++) {
//                    int conflicts = conflicts(r, worstQueenColumn);
//                    if (conflicts == minConflicts) {
//                        candidates.add(r);
//                    } else if (conflicts < minConflicts) {
//                        minConflicts = conflicts;
//                        candidates.clear();
//                        candidates.add(r);
//                    }
//                }
//
//                if (!candidates.isEmpty()) {
//                    rows[worstQueenColumn] =
//                            candidates.get(random.nextInt(candidates.size()));
//                }
//
//                moves++;
//                if (moves == rows.length * 2) {
//                    // Trying too long... start over.
//                    scramble();
//                    moves = 0;
//                }
//            }
//        }
//
//        /**
//         * Prints the board, crudely, to a print stream.
//         */
//        void print(PrintStream stream) {
//            for (int r = 0; r < rows.length; r++) {
//                for (int c = 0; c < rows.length; c++) {
//                    stream.print(rows[c] == r ? 'Q' : '.');
//                }
//                stream.println();
//            }
//        }
//    }
//
//    /**
//     * Runs the application.
//     */
//    public static void main(String[] args) {
//        Board board = new Board(2500);
//        long start = System.currentTimeMillis();
//        board.solve();
//        long stop = System.currentTimeMillis();
//        System.out.println("Found in " + ((double)(stop-start))/1000 + "s.");
//        board.print(System.out);
//    }
//}