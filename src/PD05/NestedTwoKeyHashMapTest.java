package PD05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NestedTwoKeyHashMapTest {

    private TwoKeyMap<String, String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new NestedTwoKeyHashMap<>();
    }

    @Test
    void putAndGetShouldWork() {

        map.put("Alice", "Math", 5);

        assertEquals(
                5,
                map.get("Alice", "Math")
        );
    }

    @Test
    void sizeShouldIncrease() {

        map.put("Alice", "Math", 5);
        map.put("Alice", "CS", 4);

        assertEquals(2, map.size());
    }

    @Test
    void removeShouldDeleteEntry() {

        map.put("Alice", "Math", 5);

        map.remove("Alice", "Math");

        assertNull(
                map.get("Alice", "Math")
        );
    }

    @Test
    void clearShouldEmptyMap() {

        map.put("Alice", "Math", 5);

        map.clear();

        assertTrue(map.isEmpty());
    }

    @Test
    void containsKeysShouldWork() {

        map.put("Alice", "Math", 5);

        assertTrue(
                map.containsKeys(
                        "Alice",
                        "Math"
                )
        );
    }
}
