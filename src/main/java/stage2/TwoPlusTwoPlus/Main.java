package stage2.TwoPlusTwoPlus;

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
    public int calculator(String[] input) {
        int first, second;

        if(input.length == 1) {
            return Integer.parseInt(input[0]);
        }
        else {
            first = Integer.parseInt(input[0]);
            second = Integer.parseInt(input[1]);
            return first + second;
        }
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
            else if (inputNumbers[0].isBlank()) {
                continue;
            }
            else {
                int sum = calculator.calculator(inputNumbers);
                System.out.println(sum);
            }
        }
    }
}
