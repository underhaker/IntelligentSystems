import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstra {

    public int calculateShortestPathUsingDijkstra(int n, int source, int target, int[][] adjMatrix) {
        int[] dist = new int[n + 1];
        int[] used = new int[n + 1];
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(n, new Node());

        for (int i = 1; i <= n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        priorityQueue.add(new Node(source, 0));
        dist[source] = 0;
        while (!priorityQueue.isEmpty()) {
            int evaluatedNode = priorityQueue.remove().getNode();
            used[evaluatedNode] = 1;
            for (int destinationNode = 1; destinationNode <= n; destinationNode++) {
                if (used[destinationNode] == 0 && adjMatrix[evaluatedNode][destinationNode] != Integer.MAX_VALUE) {
                    if (dist[evaluatedNode] + adjMatrix[evaluatedNode][destinationNode] < dist[destinationNode]) {
                        dist[destinationNode] = dist[evaluatedNode] + adjMatrix[evaluatedNode][destinationNode];
                    }
                    if (destinationNode == target) {
                        return dist[target];
                    }
                    priorityQueue.add(new Node(destinationNode, dist[destinationNode]));
                }
            }
        }
        return dist[target];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int source = scanner.nextInt();
        int target = scanner.nextInt();
        int[][] adjMatrix = new int[n][n];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                adjMatrix[i][j] = Integer.MAX_VALUE;
        int x, y, cost;
        for (int i = 1; i <= m; i++) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            cost = scanner.nextInt();
            adjMatrix[x][y] = cost;
            adjMatrix[y][x] = cost;
        }
        Dijkstra dijkstra = new Dijkstra();
        int shortestPath = dijkstra.calculateShortestPathUsingDijkstra(n, source, target, adjMatrix);
        if (shortestPath == Integer.MAX_VALUE) {
            System.out.println("None");
        } else {
            System.out.println(shortestPath);
        }
    }

    class Node implements Comparator<Node> {
        private int node;
        private int cost;

        public Node() {
        }

        public Node(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }

        public int getNode() {
            return node;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public int compare(Node node1, Node node2) {
            return Integer.compare(node1.cost, node2.cost);
        }
    }
}
