package PD06.Z2;

import java.util.Scanner;
import java.util.function.BiFunction;

public class CalculatorDemo {
    static int calculate(int a, int b , BiFunction<Integer, Integer, Integer> op) {
        return op.apply(a, b);
    }
    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<Integer, Integer, Integer> subtract = (a, b) -> a - b;
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        BiFunction<Integer, Integer, Integer> divide = (a, b) -> {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            return a / b;
        };
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number: ");
        int a = sc.nextInt();
        System.out.print("Enter second number: ");
        int b = sc.nextInt();
        System.out.print("Enter operation (+ - * /): ");
        String operator = sc.next();

        BiFunction<Integer, Integer, Integer> chosen;
        switch (operator) {
            case "+":
                chosen = add;
                break;
                case "-":
                    chosen = subtract;
                    break;
                    case "*":
                        chosen = multiply;
                        break;
                        case "/":
                            chosen = divide;
                            break;
                            default:
                                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        int result = calculate(a, b, chosen);
        System.out.println("Result: " + result);
    }
}
