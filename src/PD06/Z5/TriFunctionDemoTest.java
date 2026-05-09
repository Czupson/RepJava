package PD06.Z5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriFunctionDemoTest {

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @Test
    void shouldCalculateWeightedAveragePart() {

        TriFunction<Double, Double, Double, Double>
                weightedAverage =
                (grade, weight, totalWeight) ->
                        grade * weight / totalWeight;

        double result = weightedAverage.apply(5.0, 3.0, 6.0);

        assertEquals(2.5, result);
    }
}