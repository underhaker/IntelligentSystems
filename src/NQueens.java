import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NQueens {

    private static Random random = new Random();
    private static int iterations = 0;

    private static ChessBoard moveQueen(ChessBoard chessBoard, int col) {

        int bestRow = chessBoard.getRowWithLeastCollisions(col);
        int q = chessBoard.queens[col];
        System.out.println("bestRow:" + (bestRow) + "....worstRow:" + (col));
        chessBoard = updateRow(chessBoard, q, bestRow, col);
        chessBoard = updateFirstDiagonal(chessBoard, chessBoard.queenBoardIndex(col), bestRow * chessBoard.queens.length + col);
        chessBoard = updateSecondDiagonal(chessBoard, chessBoard.queenBoardIndex(col), bestRow * chessBoard.queens.length + col);
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
            chessBoard.conflictsOnPosition[fromRow * chessBoard.queens.length + currentCol]--;
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
        for (int i = 0; i < len; i++) {
            histogram[i] = len;
        }

        for (int row = 1; row < len; row++) {
            for (int col = 0; col < len; col++) {
                if (row > col) {
                    if (row + col >= len) {
                        histogram[row * len + col] = 1;
                    } else {
                        histogram[row * len + col] = 2;
                    }
                } else {
                    if (row + col < len) {
                        histogram[row * len + col] = 3;
                    } else {
                        histogram[row * len + col] = 2;
                    }
                }
            }
        }

//        for (int i = 0; i < len; i++) {
//            for (int j = 0; j < len; j++) {
//                System.out.print(histogram[i * len + j] + " ");
//            }
//            System.out.println();
//        }
        return histogram;
    }

    private static ChessBoard fillChessBoard(int n) {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.queens = new int[n];
        for (int i = 0; i < n; i++) {
            chessBoard.queens[i] = 0;
        }
        return chessBoard;
    }

    private static void minConflicts(ChessBoard chessBoard) {
        while (!chessBoard.solved()) {
            iterations++;
            int col = chessBoard.getColumnWithMostCollisions();
            chessBoard = moveQueen(chessBoard, col);
        }
    }

    public static void main(String[] args) {
        int n = 4;
        ChessBoard chessBoard = fillChessBoard(n);
        chessBoard.conflictsOnPosition = createHistogram(chessBoard);
        minConflicts(chessBoard);
        System.out.println(chessBoard.toString());

    }

    static class ChessBoard {
        public int[] queens;
        public int[] conflictsOnPosition;
        private Random random = new Random();

        public int getColumnWithMostCollisions() {
            List<Integer> big = new ArrayList<>();
            big.add(0);
            int currentQueenBoardIndex = queenBoardIndex(0);
            int largest = conflictsOnPosition[currentQueenBoardIndex];

            for (int col = 1; col < queens.length; col++) {
                currentQueenBoardIndex = queenBoardIndex(col);
                if (largest < conflictsOnPosition[currentQueenBoardIndex]) {
                    big.clear();
                    big.add(col);
                    largest = conflictsOnPosition[currentQueenBoardIndex];
                } else if (largest == conflictsOnPosition[currentQueenBoardIndex]) {
                    big.add(col);
                }
            }
            return big.get(random.nextInt(big.size()));
        }
        public int getRowWithLeastCollisions(int col) {
            List<Integer> small = new ArrayList<>();
            small.add(col);
            int leastVal = conflictsOnPosition[col];

            for (int i = col + queens.length; i < conflictsOnPosition.length; i += queens.length) {
                if (leastVal > conflictsOnPosition[i]) {
                    small.clear();
                    small.add(i);
                    leastVal = conflictsOnPosition[i];
                } else if (leastVal == conflictsOnPosition[i]) {
                    small.add(i);
                }
            }
            return small.get(random.nextInt(small.size())) / queens.length;
        }

        public int queenBoardIndex(int col) {
            return (queens.length * queens[col] + col);
        }

        public boolean solved() {
            for (int col = 0; col < queens.length; col++) {
                if (conflictsOnPosition[queenBoardIndex(col)] != 1) {
                    System.out.println("========");
                    System.out.println(toString());
                    System.out.println("========");
                    for (int i = 0; i < queens.length; i++) {
                        for (int j = 0; j < queens.length; j++) {
                            System.out.print(conflictsOnPosition[i * queens.length + j] + " ");
                        }
                        System.out.println();
                    }
                    return false;
                }
            }
            System.out.println("FINAL");
            for (int i = 0; i < queens.length; i++) {
                for (int j = 0; j < queens.length; j++) {
                    System.out.print(conflictsOnPosition[i * queens.length + j] + " ");
                }
                System.out.println();
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            System.out.println("prudnqq");
            for (int col = 0; col < queens.length; col++) {
                System.out.print(queens[col] + " ");
            }
            System.out.println();
            for (int row = 0; row < queens.length; row++) {
                for (int col = 0; col < queens.length; col++) {
                    stringBuilder.append(queens[col] == row ? " Q " : " - ");
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
