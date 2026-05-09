package PD06.Z2;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NormalizeLoginDemoTest {

    @Test
    void shouldNormalizeLogin() {

        Function<String, String> trim = s -> s.trim();
        Function<String, String> lower = s -> s.toLowerCase();
        Function<String, String> removeSpaces = s -> s.replace(" ", "");

        Function<String, String> normalize =
                trim.andThen(lower)
                        .andThen(removeSpaces);

        String result = normalize.apply("  Ja n Kow ");

        assertEquals("jankow", result);
    }
}