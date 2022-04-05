package stage6.Variables;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Print print = new Print();
        print.print();
    }
}

interface Regex {
    String letterRegex = "[A-z]+";
    String commandRegex = "[/][A-z]*";
    String digitRegex = "[-?+]?[0-9]+";
    String operatorRegex = "[-]+|[+]+";
    String assignValueRegex = "[A-z]+[=][-?+]?[0-9]+";
    String assignLetterRegex = "[A-z]+[=][A-z]+";
    String letterOrDigitRegex = "[A-z]+|[-?+]?[0-9]+";
}

class Input {
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.contains("=")) {
            return input.replaceAll("[ ]+", "");
        }
        else {
            return input.replaceAll("[ ]+", " ");
        }
    }
}

class Check implements Regex {

    public Boolean checkAssignment(String input, TreeMap<String, Integer> variables) {
        String[] assignment = input.split("=");
        String variable = assignment[0];
        String value = assignment[1];

        if (assignment.length > 2) {
            System.out.println("Invalid assignment");
            return false;
        }
        else if (input.matches(Regex.assignValueRegex)) {
            return true;
        }
        else if (input.matches(Regex.assignLetterRegex)) {
            if (variables.containsKey(value)) {
                return true;
            }
            else {
                System.out.println("Unknown variable");
                return false;
            }
        }
        else if (!variable.matches(Regex.letterRegex)) {
            System.out.println("Invalid identifier");
            return false;
        }
        else {
            System.out.println("Invalid assignment");
            return false;
        }
    }

    public void checkCommand(String expression) {
        if (expression.matches(Regex.commandRegex)) {
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
        else {
            System.out.println("Invalid expression");
        }
    }

    public void checkVariable(String expression, Map<String, Integer> variables) {
        if (variables.containsKey(expression)) {
            System.out.println(variables.get(expression));
        }
        else {
            System.out.println("Unknown variable");
        }
    }

    public Boolean checkExpression(String input, Map<String, Integer> variables) {
        String[] expression = input.split(" ");
        boolean result = false;

        for (String next : expression) {
            if (next.matches(Regex.digitRegex) || variables.containsKey(next) || next.matches(Regex.operatorRegex)) {
                result = true;
            }
            else {
                System.out.println("Unknown variable");
                return false;
            }
        }
        return result;
    }
}

class Calculator {
    public void calculateExpression(String input, Map<String, Integer> variables) {
        String[] expression = input.split(" ");
        int result = 0;
        int digit;
        String operator = "+";

        for (String next : expression) {
            if (next.matches(Regex.letterOrDigitRegex)) {
                digit = variables.containsKey(next) ? variables.get(next) : Integer.parseInt(next);
                result = operator.equals("+") ? result+digit : result-digit;
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
        System.out.println(result);
    }
}

class VariablesMap {
    public void updateVariables(String input, Map<String, Integer> variables) {
        String[] assignment = input.split("=");
        String variable = assignment[0];
        String value = assignment[1];
        int newValue;

        if (variables.containsKey(variable) && variables.containsKey(value)) {
            newValue = variables.get(value);
            variables.replace(variable, newValue);
        }
        else if (variables.containsKey(variable)) {
            newValue = Integer.parseInt(value);
            variables.replace(variable, newValue);
        }
        else if (variables.containsKey(value)) {
            newValue = variables.get(value);
            variables.put(variable, newValue);
        }
        else {
            newValue = Integer.parseInt(value);
            variables.put(variable, newValue);
        }
    }
}

class Print {
    Input input = new Input();
    Check check = new Check();
    Calculator calculator = new Calculator();
    VariablesMap variablesMap = new VariablesMap();

    public void print() {
        String expression;
        TreeMap<String, Integer> variables = new TreeMap<>();

        do {
            expression = input.getInput();

            while (expression.isBlank()) {
                expression = input.getInput();
            }

            if (expression.contains("=")) {
                if (check.checkAssignment(expression, variables)) {
                    variablesMap.updateVariables(expression, variables);
                }
            }
            else if (expression.matches(Regex.letterRegex)) {
                check.checkVariable(expression, variables);
            }
            else if (expression.matches(Regex.commandRegex)) {
                check.checkCommand(expression);
            }
            else if (check.checkExpression(expression, variables)) {
                calculator.calculateExpression(expression, variables);
            }
        } while (!expression.equals("/exit"));
    }
}
