package PD06.Z2;

import java.util.function.Function;

public class NormalizeLoginDemo {
    public static void main(String[] args) {
        Function<String, String> trim = s ->s.trim();
        Function<String, String> toLower = s -> s.toLowerCase();
        Function<String, String> removeSpaces = s -> s.replace(" ", "");
        Function<String, String> normalizeLogin = trim.andThen(toLower).andThen(removeSpaces);

        String[] loginy = {"  Adam  ", "ANIA K  ", "  k o w a l "};
        String[] normalized = new String[loginy.length];
        for (int i = 0; i < loginy.length; i++) {
            normalized[i] = normalizeLogin.apply(loginy[i]);
        }
        System.out.println("Znormalizowane loginy: ");
        for (String login : normalized) {
            System.out.println(login);
        }
    }
}
