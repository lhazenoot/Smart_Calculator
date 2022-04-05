package stage7.GotThePower;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Print print = new Print();
        print.print();
    }
}

interface Regex {
    String letter = "\\p{Alpha}+";
    String digit = "[-+]?[0-9]+";
    String command = "[/]\\p{Alpha}+";
    String operators = "[\\p{Punct}&&[^()]]+";
    String wrongOperators = ".*[*/^]{2,}.*";
    String plusAndMind = "[-+]";
    String brackets = "[()]";
    String bracketLeft = "[(]";
    String bracketRight = "[)]";
}

class Print implements Regex {
    Input input = new Input();
    Assignment assignment = new Assignment();
    Variables variables = new Variables();
    Command command = new Command();
    Expression expression = new Expression();
    Converter converter = new Converter();

    public void print() {
        String statement;
        Map<String, Integer> variablesMap = new TreeMap<>();

        do {
            statement = input.getInput();

            while (statement.isBlank()) {
                statement = input.getInput();
            }

            if (statement.contains("=")) {
                if (assignment.checkAssignment(statement, variablesMap)) {
                    variables.variablesMap(statement, variablesMap);
                }
            }
            else if (statement.matches(Regex.letter)) {
                variables.printVariableValue(statement, variablesMap);
            }
            else if (statement.matches(Regex.command)) {
                command.printCommand(statement);
            }
            else {
                String newExpression = expression.convertExpressionWithWhiteSpaces(statement);
                if (expression.checkExpression(newExpression, variablesMap)) {
                    String postfix = converter.getPostfix(newExpression);
                    int result = converter.calculateExpression(postfix, variablesMap);
                    System.out.println(result);
                }
            }
        } while (!statement.equals("/exit"));
    }
}

class Input {
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().replaceAll("[ ]+", "");
    }
}

class Assignment implements Regex {
    public Boolean checkAssignment(String statement, Map<String, Integer> variablesMap) {
        String[] array = statement.split("=");
        String variable = array[0];
        String value = array[1];

        boolean check = true;

        if (array.length > 2) {
            System.out.println("Invalid assignment");
            check = false;
        }
        else if (!variable.matches(Regex.letter)) {
            System.out.println("Invalid identifier");
            check = false;
        }
        else if (!value.matches(Regex.digit) && !variablesMap.containsKey(value)) {
            System.out.println("Invalid Assignment");
            check = false;
        }
        return check;
    }
}

class Variables {
    public void variablesMap(String statement, Map<String, Integer> variablesMap) {
        String[] array = statement.split("=");
        String variable = array[0];
        String value = array[1];

        if (variablesMap.containsKey(variable) && variablesMap.containsKey(value)) {
            variablesMap.replace(variable, variablesMap.get(value));
        }
        else if (variablesMap.containsKey(variable)) {
            variablesMap.replace(variable, Integer.parseInt(value));
        }
        else if (variablesMap.containsKey(value)) {
            variablesMap.put(variable, variablesMap.get(value));
        }
        else {
            variablesMap.put(variable, Integer.parseInt(value));
        }
    }

    public void printVariableValue(String statement, Map<String, Integer> variablesMap) {
        if (variablesMap.containsKey(statement)) {
            System.out.println(variablesMap.get(statement));
        }
        else {
            System.out.println("Unknown statement");
        }
    }
}

class Command {
    public void printCommand(String statement) {
        switch (statement) {
            case "/exit":
                System.out.println("Bye!");
                return;
            case "/help":
                System.out.println("The program calculates the expression, updates variables and print existing variables");
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
}

class Expression extends Converter implements Regex {
    public String convertExpressionWithWhiteSpaces(String statement) {
        String[] array = statement.split("");
        StringBuilder sb = new StringBuilder();
        String operator = "";

        for (String next : array) {
            if (sb.length() == 0 && next.matches(Regex.plusAndMind)) {
                sb.append(next);
            }
            else if (next.matches(Regex.digit) || next.matches(Regex.letter) || next.matches(brackets)) {
                if (!operator.isEmpty()) {
                    operator = convertOperator(operator);
                    sb.append(operator);
                    operator = "";
                }
                sb.append(next);
            }
            else if (next.matches(Regex.operators)) {
                operator += next;
            }
        }
        return sb.toString().replaceAll("[(]", "( ").replaceAll("[)]", " )");
    }

    public Boolean checkExpression(String expression, Map<String, Integer> variablesMap) {
        String[] array = expression.split(" ");
        int countLeftBracket = 0;
        int countRightBracket = 0;
        boolean check = true;

        if (expression.matches(Regex.wrongOperators)) {
            System.out.println("Invalid expression");
            return false;
        }

        for (String next : array) {
            if (next.matches(Regex.letter) && !variablesMap.containsKey(next)) {
                System.out.println("Unknown variable");
                return false;
            }
            else if (next.matches(Regex.bracketLeft)) {
                countLeftBracket++;
            }
            else if (next.matches(Regex.bracketRight)) {
                countRightBracket++;
            }
        }

        if (countLeftBracket != countRightBracket) {
            System.out.println("Invalid expression");
            return false;
        }
        return check;
    }
}

class Converter implements Regex {
    public String convertOperator(String operator) {
        if (operator.matches("[-]+")) {
            return operator.length() % 2 == 0 ? " + " : " - ";
        }
        else if (operator.contains("+")) {
            return operator.endsWith("-") ? " - " : " + ";
        }
        else {
            if (operator.endsWith("-")) {
                operator = operator.replace("-", " -");
                return String.format(" %s", operator);
            }
            else {
                return String.format(" %s ", operator);
            }
        }
    }

    public String getPostfix(String expression) {
        String[] array = expression.split(" ");
        Stack<String> stack = new Stack<>();
        StringJoiner sj = new StringJoiner(" ");

        for (String next : array) {
            if (next.matches(Regex.digit) || next.matches(Regex.letter)) {
                sj.add(next);
            }
            else {
                if (stack.empty()) {
                    stack.push(next);
                }
                else {
                    switch (next) {
                        case "(":
                            stack.push(next);
                            break;
                        case ")":
                            while (!stack.empty() && !stack.peek().matches("[(]")) {
                                sj.add(stack.pop());
                            }
                            stack.pop();
                            break;
                        case "^":
                            while (!stack.empty() && stack.peek().matches("[\\^]")) {
                                sj.add(stack.pop());
                            }
                            stack.push(next);
                            break;
                        case "*":
                        case "/":
                            while (!stack.empty() && stack.peek().matches("[*/^]")) {
                                sj.add(stack.pop());
                            }
                            stack.push(next);
                            break;
                        case "+":
                        case "-":
                            while (!stack.empty() && stack.peek().matches("[-+*/^]")) {
                                sj.add(stack.pop());
                            }
                            stack.push(next);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (!stack.empty()) {
            while (!stack.empty()) {
                sj.add(stack.pop());
            }
        }
        return sj.toString();
    }

    public int calculateExpression(String postfix, Map<String, Integer> variablesMap) {
        String[] array = postfix.split(" ");
        int result = 0;
        int digit;
        int digit1, digit2;
        Stack<Integer> stack = new Stack<>();

        for (String next : array) {
            if (next.matches(Regex.digit)) {
                digit = Integer.parseInt(next);
                stack.push(digit);
            }
            else if (next.matches(Regex.letter)) {
                digit = variablesMap.get(next);
                stack.push(digit);
            }
            else {
                digit2 = stack.pop();
                digit1 = stack.pop();

                switch (next) {
                    case "-":
                        result = digit1 - digit2;
                        break;
                    case "+":
                        result = digit1 + digit2;
                        break;
                    case "*":
                        result = digit1 * digit2;
                        break;
                    case "/":
                        result = digit1 / digit2;
                        break;
                    case "^":
                        result = (int) Math.pow(digit1, digit2);
                        break;
                    default:
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
}
