package PD06.Z4;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BiConsumerDemo {
    static void printReport(Map<String, Integer> map,
                            BiConsumer<String, Integer> consumer) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
    public static void main(String[] args) {
        Map<String, Integer> productToQty = new HashMap<>();
        productToQty.put("Laptop", 5);
        productToQty.put("Mobile", 18);
        productToQty.put("Tablet", 9);
        productToQty.put("Monitor", 12);

        BiConsumer<String, Integer> reporter = (name, qty) -> System.out.println(
                "Product: " + name + ", pieces: " + qty
        );
        printReport(productToQty, reporter);
    }
}
