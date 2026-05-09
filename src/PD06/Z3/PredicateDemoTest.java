package PD06.Z3;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class PredicateDemoTest {

    Predicate<String> isValidLogin =
            login -> login.matches("^[A-Za-z][A-Za-z0-9]{2,}$");

    @Test
    void shouldAcceptValidLogin() {

        assertTrue(isValidLogin.test("Adam123"));
    }

    @Test
    void shouldRejectTooShortLogin() {

        assertFalse(isValidLogin.test("x"));
    }

    @Test
    void shouldRejectPolishCharacters() {

        assertFalse(isValidLogin.test("gość"));
    }

    @Test
    void shouldRejectUnderscore() {

        assertFalse(isValidLogin.test("User_01"));
    }
}