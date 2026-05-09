package PD06.Z4;

import java.util.Random;
import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random rand = new Random();
        Supplier<String> codeSupplier = () -> {
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int index = rand.nextInt(chars.length());
                code.append(chars.charAt(index));
            }
            return code.toString();
        };
        String[] codes = new String[10];
        for (int i = 0; i < codes.length; i++) {
            codes[i] = codeSupplier.get();
        }
        System.out.println("Generated codes:");
        for (String code : codes) {
            System.out.println(code);
        }
    }
}
