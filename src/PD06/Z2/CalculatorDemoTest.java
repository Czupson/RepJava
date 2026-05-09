package PD06.Z2;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorDemoTest {

    static int calculate(int a, int b,
                         BiFunction<Integer, Integer, Integer> op) {

        return op.apply(a, b);
    }

    @Test
    void shouldAddNumbers() {

        BiFunction<Integer, Integer, Integer> add =
                Integer::sum;

        int result = calculate(10, 3, add);

        assertEquals(13, result);
    }

    @Test
    void shouldMultiplyNumbers() {

        BiFunction<Integer, Integer, Integer> mul =
                (a, b) -> a * b;

        int result = calculate(4, 5, mul);

        assertEquals(20, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {

        BiFunction<Integer, Integer, Integer> div =
                (a, b) -> {
                    if (b == 0) {
                        throw new IllegalArgumentException("Division by zero");
                    }
                    return a / b;
                };

        assertThrows(
                IllegalArgumentException.class,
                () -> calculate(7, 0, div)
        );
    }
}