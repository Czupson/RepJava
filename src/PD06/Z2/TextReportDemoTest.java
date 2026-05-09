package PD06.Z2;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextReportDemoTest {

    @Test
    void shouldCountCharactersWithoutSpaces() {

        Function<String, Integer> countWithoutSpaces =
                s -> s.replace(" ", "").length();

        int result = countWithoutSpaces.apply("Ja va");

        assertEquals(4, result);
    }

    @Test
    void shouldCountVowels() {

        Function<String, Integer> countVowels = s -> {

            int count = 0;

            for (char c : s.toCharArray()) {

                char lower = Character.toLowerCase(c);

                if (lower == 'a' ||
                        lower == 'e' ||
                        lower == 'i' ||
                        lower == 'o' ||
                        lower == 'u' ||
                        lower == 'y') {

                    count++;
                }
            }

            return count;
        };

        int result = countVowels.apply("Java");

        assertEquals(2, result);
    }
}