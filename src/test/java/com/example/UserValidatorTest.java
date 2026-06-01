package com.example;

import org.example.validation.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    private final UserValidator validator = new UserValidator();

    @Nested
    @DisplayName("Walidacja email")
    class ValidateEmailTest{
        @Test
        void shouldNotThrowException_whenEmailIsValid() {
            String email = "jan@example.com";

            assertDoesNotThrow(() -> validator.validateEmail(email));
        }

        @Test
        void shouldThrowIllegalArgumentException_whenEmailDoesNotContainAt() {
            String email = "janexample.com";

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validateEmail(email));

            assertTrue(exception.getMessage().contains("@"));
        }

        @Test
        void shouldThrowIllegalArgumentException_whenEmailIsEmpty() {
            String email = "";

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validateEmail(email)
            );

            assertTrue(exception.getMessage().contains("pusty"));
        }

        @Test
        void shouldThrowNullPointerException_whenEmailIsNull() {
            String email = null;

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> validator.validateEmail(email)
            );

            assertEquals("Email nie może być null", exception.getMessage());
        }
    }
}
