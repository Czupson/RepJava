package PD06.Z2;

import java.util.Scanner;
import java.util.function.Function;

public class TextReportDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Podaj tekst: ");
        String text = sc.nextLine();

        Function<String, Integer> countWithoutSpaces = s -> s.replace(" ", "").length();
        Function<String, Integer> countVowels = s -> {
            int count = 0;
            for (char c : s.toCharArray()) {

                char lower = Character.toLowerCase(c);

                if (lower == 'a' ||
                        lower == 'e' ||
                        lower == 'i' ||
                        lower == 'o' ||
                        lower == 'u' ||
                        lower == 'y') {

                    count++;
                }
            }

            return count;
        };

        int charsWithoutSpaces = countWithoutSpaces.apply(text);
        int vowels = countVowels.apply(text);

        System.out.println("Liczba znaków bez spacji: " + charsWithoutSpaces);
        System.out.println("Liczba samogłosek: " + vowels);
    }
}