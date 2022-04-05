package stage4.AddSubtractions;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Print print = new Print();
        print.print();

    }
}

class Input {
    public String[] getInput() {
        Scanner scanner = new Scanner(System.in);
        String regex = "[ ]+";
        return scanner.nextLine().split(regex);
    }
}

class Calculator {
    public int calculator(String[] input) {
        int result = 0;
        int digit;
        String operator = "+";
        String digitRegex = "-[0-9]+|[0-9]+";

        for (String next : input) {
            if (next.matches(digitRegex)) {
                digit = Integer.parseInt(next);
                result = operator.equals("+") ? result+digit : result-digit;
            }
            else {
                if (next.contains("-")) {
                    operator = next.length() % 2 == 0 ? "+" : "-";
                }
                else {
                    operator = "+";
                }
            }
        }
        return result;
    }
}

class Print {
    Input input = new Input();
    Calculator calculator = new Calculator();

    public void print() {
        while (true) {
            String[] expression = input.getInput();
            if (expression[0].equals("/exit")) {
                System.out.println("Bye!");
                return;
            }
            else if (expression[0].equals("/help")) {
                System.out.println("The program calculates the addition and subtraction of numbers");
            }
            else if (expression[0].isBlank()) {
                continue;
            }
            else {
                System.out.println(calculator.calculator(expression));
            }
        }
    }
}

