package PD06.Z1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RunnableDemoTest {
    @Test
    void runnableShouldRunWithoutException() {

        Runnable runnable = () -> System.out.println("Hello");

        assertDoesNotThrow(runnable::run);
    }
}
