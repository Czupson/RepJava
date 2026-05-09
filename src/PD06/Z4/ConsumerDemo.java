package PD06.Z4;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {
    static void printWithNumbers(List<String> lines, Consumer<String> consumer){
        for (int i = 0; i < lines.size(); i++){
            String text = (i +1) + ") " + lines.get(i);
            consumer.accept(text);
        }
    }
    public static void main(String[] args) {
        List<String> lines = Arrays.asList(
                "Java",
                "Python",
                "C++",
                "JavaScript"
        );
        Consumer<String> logger = s -> System.out.println(s);
        printWithNumbers(lines, logger);
    }
}
