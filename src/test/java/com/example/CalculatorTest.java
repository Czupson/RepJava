package com.example;

import org.example.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    @Nested
    class SubtractionTest {
        private Calculator calculator;

        @BeforeEach
        void setUp() {
            calculator = new Calculator();
        }

        @Test
        void shouldSubstractTwoPositiveNumbers() {
            int a = 5;
            int b = 3;

            int result = calculator.subtract(a, b);

            assertEquals(2, result);
        }

        @Test
        void shouldReturnNegativeResult() {
            assertEquals(-2, calculator.subtract(3, 5));
        }

        @Test
        void shouldSubstractZero() {
            assertEquals(5, calculator.subtract(5, 0));
        }

        @Test
        void shouldSubstractFromZero() {
            assertEquals(-5, calculator.subtract(0, 5));
        }

        @Test
        void shouldSubstractNegativeNumbers() {
            assertEquals(-1, calculator.subtract(-3, -2));
        }
    }
}
