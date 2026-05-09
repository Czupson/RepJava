package PD06.Z3;

import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class BiPredicateDemoTest {

    @Test
    void shouldCompareIgnoringCase() {

        BiPredicate<String, String> sameIgnoreCase =
                (a, b) -> a.equalsIgnoreCase(b);

        assertTrue(sameIgnoreCase.test("JAVA", "java"));
    }

    @Test
    void shouldCheckSuffix() {

        BiPredicate<String, String> isSuffix =
                (a, b) -> a.endsWith(b);

        assertTrue(isSuffix.test("Programming", "ing"));
    }
}