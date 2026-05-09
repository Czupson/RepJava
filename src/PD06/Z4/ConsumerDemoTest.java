package PD06.Z4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsumerDemoTest {

    @Test
    void consumerShouldAcceptText() {

        List<String> output = new ArrayList<>();

        Consumer<String> consumer = output::add;

        consumer.accept("Hello");

        assertEquals(List.of("Hello"), output);
    }
}