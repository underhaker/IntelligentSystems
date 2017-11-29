import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

class Solution implements Comparator<Solution> {
    private int[][] solution;
    private int heuristicFunction;
    private int realDistance;
    private int movesCounter;
    private String position;
    private Solution previousSolution;

    public Solution getPreviousSolution() {
        return previousSolution;
    }

    public void setPreviousSolution(Solution previousSolution) {
        this.previousSolution = previousSolution;
    }

    public int getMovesCounter() {
        return movesCounter;
    }

    public void setMovesCounter(int movesCounter) {
        this.movesCounter = movesCounter;
    }

    public Solution() {
    }

    public Solution(int[][] solution, int heuristicFunction, int realDistance, int movesCounter, String position, Solution previousPosition) {
        this.solution = solution;
        this.heuristicFunction = heuristicFunction;
        this.realDistance = realDistance;
        this.position = position;
        this.previousSolution = previousPosition;
        this.movesCounter = movesCounter;
    }


    public int getRealDistance() {
        return realDistance;
    }

    public void setRealDistance(int realDistance) {
        this.realDistance = realDistance;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }

    public int getHeuristicFunction() {
        return heuristicFunction;
    }

    public void setHeuristicFunction(int heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int compare(Solution solution1, Solution solution2) {
        return Integer.compare(solution1.heuristicFunction, solution2.heuristicFunction);
    }
}

public class AStar {
    private static Set<String> used;
    private PriorityQueue<Solution> priorityQueue;
    private int n;
    private int[][] finalSolution1;
    private int[][] finalSolution2;


    public AStar(int blockSize) {
        this.used = new HashSet<>();
        this.priorityQueue = new PriorityQueue<>(blockSize + 1, new Solution());
        this.n = (int) Math.sqrt(blockSize + 1);
        this.finalSolution1 = new int[this.n][this.n];
        this.finalSolution2 = new int[this.n][this.n];
        generateFinalSolutions();
    }

    private void generateFinalSolutions() {
        int number = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                finalSolution2[i][j] = number;
                finalSolution1[i][j] = number + 1;
                number++;
            }
        }
        finalSolution1[n - 1][n - 1] = 0;
    }

    private int heuristicCalculation(int[][] currentSolution, int distance) {
        int heuristicFunction = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (currentSolution[i][j] != finalSolution1[i][j]) {
                    heuristicFunction++;
                }
            }
        }
        return heuristicFunction + distance;
    }

    private int heuristicManhattanCalculation(int[][] currentSolution, int distance) {
        int heuristicFunction = 0;
        boolean foundOriginal;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                foundOriginal = false;
                for (int k = 0; k < n; k++) {
                    if (foundOriginal) break;
                    for (int p = 0; p < n; p++) {
                        if (currentSolution[i][j] == finalSolution1[k][p]) {
                            heuristicFunction+=Math.abs(i - k) + Math.abs(j - p);
                            foundOriginal = true;
                            break;
                        }
                    }
                }
            }
        }
        return heuristicFunction + distance;
    }

    private String toString(int[][] solution) {
        StringBuilder arrayToString = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arrayToString.append(solution[i][j]);
            }
        }
        return arrayToString.toString();
    }

    private int[][] cloneArray(int[][] array) {
        int[][] newArray = array.clone();
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = newArray[i].clone();
        }
        return newArray;
    }

    public void aStar(int[][] inputSolution) {
        int[][] newSolution;
        int[][] currentSolution;
        priorityQueue.add(new Solution(inputSolution, heuristicCalculation(inputSolution, 0), 0, 0, null, null));
        boolean foundZero;
        Solution solution;
        int distance;
        while (!priorityQueue.isEmpty()) {
            solution = priorityQueue.remove();
            currentSolution = cloneArray(solution.getSolution());
            distance = solution.getRealDistance();
            String solutionString = toString(currentSolution);
            used.add(solutionString);
            if (isFinalSolution(currentSolution)) {
                printSolution(solution);
                return;
            }
            foundZero = false;
            for (int i = 0; i < n; i++) {
                if (foundZero) break;
                for (int j = 0; j < n; j++) {
                    if (currentSolution[i][j] == 0) {
                        foundZero = true;
                        //up
                        if (i != 0) {
                            newSolution = swapNumbers(currentSolution, i, j, i - 1, j);
                            if (!used.contains(toString(newSolution))) {
                                priorityQueue.add(new Solution(newSolution, heuristicManhattanCalculation(newSolution, distance + 1), distance + 1, solution.getMovesCounter() + 1, "down", solution));
                            }
                        }
                        //down
                        if (i != n - 1) {
                            newSolution = swapNumbers(currentSolution, i, j, i + 1, j);
                            if (!used.contains(toString(newSolution))) {
                                priorityQueue.add(new Solution(newSolution, heuristicManhattanCalculation(newSolution, distance + 1), distance + 1, solution.getMovesCounter() + 1, "up", solution));

                            }
                        }
                        //left
                        if (j != 0) {
                            newSolution = swapNumbers(currentSolution, i, j, i, j - 1);
                            if (!used.contains(toString(newSolution))) {
                                priorityQueue.add(new Solution(newSolution, heuristicManhattanCalculation(newSolution, distance + 1), distance + 1, solution.getMovesCounter() + 1, "right", solution));
                            }
                        }
                        //right
                        if (j != n - 1) {
                            newSolution = swapNumbers(currentSolution, i, j, i, j + 1);
                            if (!used.contains(toString(newSolution))) {
                                priorityQueue.add(new Solution(newSolution, heuristicManhattanCalculation(newSolution, distance + 1), distance + 1, solution.getMovesCounter() + 1, "left", solution));
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void printSolution(Solution solution) {
        System.out.println(solution.getMovesCounter());
        System.out.println(solution.getPosition());
        while (solution.getPreviousSolution() != null) {
            if (solution.getPreviousSolution().getPosition() != null) {
                System.out.println(solution.getPreviousSolution().getPosition());
            }
            solution = solution.getPreviousSolution();
        }
    }

    private int[][] swapNumbers(int[][] array, int x1, int y1, int x2, int y2) {
        int[][] tempArray = cloneArray(array);
        int temp = tempArray[x1][y1];
        tempArray[x1][y1] = tempArray[x2][y2];
        tempArray[x2][y2] = temp;
        return tempArray;
    }


    private boolean isFinalSolution(int[][] inputSolution) {
        Boolean isFinalSolution1 = null;
        Boolean isFinalSolution2 = null;
        for (int i = 0; i < n; i++) {
            if (isFinalSolution1 != null && !isFinalSolution1) break;
            for (int j = 0; j < n; j++) {
                if (inputSolution[i][j] != finalSolution1[i][j]) {
                    isFinalSolution1 = false;
                    break;
                }
            }
        }
        if (isFinalSolution1 == null) {
            isFinalSolution1 = true;
        }
        for (int i = 0; i < n; i++) {
            if (isFinalSolution2 != null && !isFinalSolution2) break;
            for (int j = 0; j < n; j++) {
                if (inputSolution[i][j] != finalSolution2[i][j]) {
                    isFinalSolution2 = false;
                    break;
                }
            }
        }
        if (isFinalSolution2 == null) {
            isFinalSolution2 = true;
        }
        return isFinalSolution1 | isFinalSolution2;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
//        System.out.println(n);
//        int n = 8;
        int size = (int) Math.sqrt(n + 1);
//        System.out.println(size);

//        1 2 3
//        4 5 6
//        0 7 8
//        int[][] inputSolution = new int[][]{{1, 2, 3}, {4, 5, 6}, {0, 7, 8}};
        int[][] inputSolution = new int[size][size];
//        int[][] inputSolution = new int[][]{{7, 2, 1}, {0, 3, 8}, {4, 6, 5}};
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                inputSolution[i][j] = scanner.nextInt();
//                System.out.println(inputSolution[i][j]);
            }
        }
        AStar aStar = new AStar(n);
        aStar.aStar(inputSolution);
    }
}
/*
8
1 2 3
4 5 6
0 7 8

8
8 0 7
2 4 5
1 3 6

8
4 6 3
0 2 1
7 8 5
 */