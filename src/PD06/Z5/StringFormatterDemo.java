package PD06.Z5;

public class StringFormatterDemo {
    @FunctionalInterface
    interface StringFormatter {
        String format(String input);
    }
    static String applyFormat(String text, StringFormatter formatter) {
        return formatter.format(text);
    }

    public static void main(String[] args) {
        String text = "Java";
        StringFormatter upperCase = s -> s.toUpperCase();
        StringFormatter addPrefix = s -> ">>> " + s;
        StringFormatter reverse = s -> new StringBuilder(s)
                .reverse().toString();
        System.out.println(applyFormat(text, upperCase));
        System.out.println(applyFormat(text, addPrefix));
        System.out.println(applyFormat(text, reverse));
    }
}
