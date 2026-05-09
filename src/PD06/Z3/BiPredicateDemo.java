package PD06.Z3;

import java.util.Scanner;
import java.util.function.BiPredicate;

public class BiPredicateDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the first text: ");
        String text = sc.nextLine();
        System.out.print("Enter the second text: ");
        String text2 = sc.nextLine();

        BiPredicate<String, String> sameIgnoreCase = (a, b) -> a.equalsIgnoreCase(b);
        BiPredicate<String, String> isSuffix = (a, b) -> a.endsWith(b);
        boolean same = sameIgnoreCase.test(text, text2);
        boolean sufix = isSuffix.test(text, text2);
        System.out.println("sameIgnoreCase: " + same);
        System.out.println("isSuffix: " + isSuffix);
    }
}
