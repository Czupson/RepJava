package com.example;

import org.example.roman.RomanNumerals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RomanNumeralsTest {

    @ParameterizedTest
    @CsvSource({
            "1, I",
            "2, II",
            "3, III",
            "4, IV",
            "5, V",
            "9, IX",
            "10, X",
            "40, XL",
            "58, LVIII",
            "1994, MCMXCIV",
            "3999, MMMCMXCIX"
    })
    void shouldConvertArabicNumberToRoman(int number, String expected) {

        String result = RomanNumerals.toRoman(number);

        assertEquals(expected, result);
    }
    @Test
    void shouldThrowException_whenNumberIsZero() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RomanNumerals.toRoman(0)
        );
    }

    @Test
    void shouldThrowException_whenNumberIsNegative() {

        assertThrows(
                IllegalArgumentException.class,
                () -> RomanNumerals.toRoman(-5)
        );
    }
}