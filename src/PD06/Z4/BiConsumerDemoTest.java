package PD06.Z4;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiConsumerDemoTest {

    @Test
    void shouldAcceptKeyAndValue() {

        Map<String, Integer> result = new HashMap<>();

        BiConsumer<String, Integer> consumer = result::put;

        consumer.accept("Laptop", 5);

        assertEquals(5, result.get("Laptop"));
    }
}