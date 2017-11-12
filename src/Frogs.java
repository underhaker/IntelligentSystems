import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Frogs {
    private Stack<String> stack;
    private static String FINAL_POSITION;
    private static Set<String> used;

    public Frogs() {
        stack = new Stack<>();
        used = new HashSet<>();
    }

    public void calculateFrogsMovement(int n) {
        StringBuilder initialPosition = new StringBuilder();
        for (int i = 0; i < n; i++) {
            initialPosition.append(">");
        }
        initialPosition.append("_");
        for (int i = 0; i < n; i++) {
            initialPosition.append("<");
        }
        String initial = initialPosition.toString();
        FINAL_POSITION = initialPosition.reverse().toString();
        dfs(initial);
    }

    private void dfs(String frogsPosition) {
        stack.push(frogsPosition);
        used.add(frogsPosition);
        if (frogsPosition.equals(FINAL_POSITION)) {
            printMovements(stack);
            System.exit(0);
        }
        String result = calculateFirstPosition(frogsPosition);
        if (!result.equals("NONE") && !used.contains(result)) {
            dfs(result);
            stack.pop();
        }
        result = calculateSecondPosition(frogsPosition);
        if (!result.equals("NONE") && !used.contains(result)) {
            dfs(result);
            stack.pop();
        }
        result = calculateThirdPosition(frogsPosition);
        if (!result.equals("NONE") && !used.contains(result)) {
            dfs(result);
            stack.pop();
        }
        result = calculateForthPosition(frogsPosition);
        if (!result.equals("NONE") && !used.contains(result)) {
            dfs(result);
            stack.pop();
        }
    }

    private void printMovements(Stack<String> stack) {
        Collections.reverse(stack);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    private String calculateFirstPosition(String frogsPosition) {
        if (frogsPosition.contains(">_")) {
            return frogsPosition.replaceFirst(">_", "_>");
        }
        if (frogsPosition.contains("><_")) {
            return frogsPosition.replaceFirst("><_", "_<>");
        }
        return "NONE";
    }

    private String calculateSecondPosition(String frogsPosition) {
        if (frogsPosition.contains(">>_")) {
            return frogsPosition.replaceFirst(">>_", "_>>");
        }
        return "NONE";
    }

    private String calculateThirdPosition(String frogsPosition) {
        if (frogsPosition.contains("_<")) {
            return frogsPosition.replaceFirst("_<", "<_");
        }
        return "NONE";
    }

    private String calculateForthPosition(String frogsPosition) {
        if (frogsPosition.contains("_<<")) {
            return frogsPosition.replaceFirst("_<<", "<<_");
        }
        if (frogsPosition.contains("_><")) {
            return frogsPosition.replaceFirst("_><", "<>_");
        }
        return "NONE";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Frogs frogs = new Frogs();
        frogs.calculateFrogsMovement(scanner.nextInt());
    }
}
