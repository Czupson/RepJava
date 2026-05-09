package PD06.Z5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringFormatterDemoTest {

    @FunctionalInterface
    interface StringFormatter {
        String format(String input);
    }

    @Test
    void shouldConvertToUpperCase() {

        StringFormatter formatter = String::toUpperCase;

        String result = formatter.format("java");

        assertEquals("JAVA", result);
    }

    @Test
    void shouldReverseString() {

        StringFormatter reverse =
                s -> new StringBuilder(s)
                        .reverse()
                        .toString();

        String result = reverse.format("Java");

        assertEquals("avaJ", result);
    }
}