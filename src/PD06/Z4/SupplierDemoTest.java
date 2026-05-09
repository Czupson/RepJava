package PD06.Z4;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDemoTest {

    @Test
    void shouldGenerateCodeWithLengthSix() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random = new Random();

        Supplier<String> supplier = () -> {

            StringBuilder code = new StringBuilder();

            for (int i = 0; i < 6; i++) {

                int index = random.nextInt(chars.length());

                code.append(chars.charAt(index));
            }

            return code.toString();
        };

        String code = supplier.get();

        assertEquals(6, code.length());
    }
}