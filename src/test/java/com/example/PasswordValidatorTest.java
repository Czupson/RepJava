package com.example;

import org.example.validation.PasswordValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasswordValidatorTest {
    @ParameterizedTest
    @CsvSource({
            "Password1, true",
            "pass, false",
            "password, false",
            "password1, false",
            "PASSWORD1, true",
            "Pass1234, true",
            "Aa1, false"
    })
    void shouldValidatePasswordCorrectly(String password, boolean expected) {
        boolean result = PasswordValidator.isValid(password);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnFalse_whenPasswordIsNullOrEmpty(String password) {
        boolean result = PasswordValidator.isValid(password);

        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short1",
            "nouppercase1",
            "NO_DIGITS"
    })
    void shouldReturnFalse_whenPasswordIsInvalid(String password) {
        boolean result = PasswordValidator.isValid(password);

        assertFalse(result);
    }
}
