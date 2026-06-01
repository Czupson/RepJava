package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.example.StringUtils;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {
    @Test
    void shouldReturnTrue_whenTextIsPalindrome() {
        String text = "kajak";

        boolean result = StringUtils.isPalindrome(text);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrue_whenPalindromeHasDifferentLetterCases() {
        String text = "Kajak";

        boolean result = StringUtils.isPalindrome(text);

        assertTrue(result);

    }

    @Test
    void shouldReturnFalse_whenTextIsNotPalindrome() {
        String text = "java";

        boolean result = StringUtils.isPalindrome(text);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrue_whenTextIsEmpty() {
        String text = "";

        boolean result = StringUtils.isPalindrome(text);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrue_whenTextIsNull() {
        String text = null;

        boolean result = StringUtils.isPalindrome(text);

        assertTrue(result);
    }

}
