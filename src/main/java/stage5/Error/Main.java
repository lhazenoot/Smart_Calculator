package stage5.Error;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Print print = new Print();
        print.print();
    }
}

class Input {
    public String getExpression() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().replaceAll("[ ]+", " ");
    }
}

interface Pattern {
    String digitRegex = "[-?+]?[0-9]+";
    String operatorRegex = "[-]+|[+]+";
    String commandRegex = "[/][A-z]*";
}

class Check implements Pattern {
    public Boolean expressionIsValid(String expressionAsString) {
        boolean digit = false;
        boolean operator = false;
        String[] expression = expressionAsString.split(" ");

        for (String next : expression) {
            if (next.matches(Pattern.digitRegex)) {
                digit = true;
                if (expression.length == 1) {
                    operator = true;
                }
            }
            else if (next.matches(Pattern.operatorRegex)) {
                operator = true;
            }
            else {
                return false;
            }
        }
        return digit && operator;
    }

    public void checkCommand(String expression) {
        if (expression.matches(Pattern.commandRegex)) {
            if (expression.equals("/exit")) {
                System.out.println("Bye!");
            }
            else if (expression.equals("/help")) {
                System.out.println("The program calculates the addition and subtraction of numbers");
            }
            else {
                System.out.println("Unknown command");
            }
        }
        else if (expression.isBlank()) {
        }
        else {
            System.out.println("Invalid expression");
        }
    }
}

class Calculator implements Pattern {
    public void calculateExpression(String expressionAsString) {
        String[] expression = expressionAsString.split(" ");
        int sum = 0;
        int digit;
        String operator = "+";

        for (String next : expression) {
            if (next.matches(Pattern.digitRegex)) {
                digit = Integer.parseInt(next);
                sum = operator.equals("+") ? sum+digit : sum-digit;
            }
            else {
                if (next.matches("[-]+")) {
                    operator = next.length() % 2 == 0 ? "+" : "-";
                }
                else {
                    operator = "+";
                }
            }
        }
        System.out.println(sum);
    }
}

class Print {
    Input input = new Input();
    Check check = new Check();
    Calculator calculator = new Calculator();

    public void print() {
        String expression;

        do {
            expression = input.getExpression();

            if (check.expressionIsValid(expression)) {
                calculator.calculateExpression(expression);
            }
            else {
                check.checkCommand(expression);
            }
        } while (!expression.equals("/exit"));
    }
}




