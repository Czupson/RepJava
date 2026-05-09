package PD06.Z2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class OperatorDemoTest {

    @Test
    void shouldRemoveNegativeNumbers() {

        UnaryOperator<List<Integer>> removeNegatives = list -> {
            list.removeIf(x -> x < 0);
            return list;
        };

        List<Integer> nums =
                new ArrayList<>(Arrays.asList(3, -1, 7, -5));

        List<Integer> result = removeNegatives.apply(nums);

        assertEquals(List.of(3, 7), result);
        assertSame(nums, result);
    }

    @Test
    void shouldReturnMaximumNumber() {

        BinaryOperator<Integer> maxOp = Integer::max;

        int result = maxOp.apply(10, 7);

        assertEquals(10, result);
    }
}