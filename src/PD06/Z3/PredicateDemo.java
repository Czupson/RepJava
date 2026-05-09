package PD06.Z3;

import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        String[] loginy = {"adam", "Ala123", "x", "User_01", "ADMIN", "gość"};
        Predicate<String> isValidLogin = login -> login.matches("^[A-Za-z][A-Za-z0-9]{2,}$");
        int validCount = 0;
        System.out.println("Valid logins: ");
        for (String login : loginy) {
            if (isValidLogin.test(login)) {
                System.out.println(login);
                validCount++;
            }
        }
        System.out.println("Number of valid logins: " + validCount);
    }
}
