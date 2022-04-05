package stage3.CountThemAll;

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
        return scanner.nextLine().split(" ");
    }
}

class Calculator {
    public void calculator(String[] input) {
        int result = 0;
        int size = input.length;

        for (String s : input) {
            result = result + Integer.parseInt(s);
        }
        System.out.println(result);
    }
}

class Print {
    Input input = new Input();
    Calculator calculator = new Calculator();

    public void print() {
        while (true) {
            String[] inputNumbers = input.getInput();
            if (inputNumbers[0].equals("/exit")) {
                System.out.println("Bye!");
                return;
            }
            else if (inputNumbers[0].equals("/help")) {
                System.out.println("The program calculates the sum of numbers");
            }
            else if (inputNumbers[0].isBlank()) {
                continue;
            }
            else {
                calculator.calculator(inputNumbers);
            }
        }
    }
}


