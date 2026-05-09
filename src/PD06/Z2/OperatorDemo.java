package PD06.Z2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class OperatorDemo {
    public static void main(String[] args) {
        UnaryOperator<List<Integer>> removeNegatives = list -> {
            list.removeIf(x -> x < 0);
            return list;
        };
        BinaryOperator<Integer> maxOp = Integer::max;

        List<Integer> nums = new ArrayList<>(
                Arrays.asList(3, -1, 7, -5, 10, 0)
        );
        System.out.println("Before: " + nums);
        removeNegatives.apply(nums);
        System.out.println("After: " + nums);

        int max = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            max = maxOp.apply(max, nums.get(i));
        }
        System.out.println("Maximum: " + max);
    }
}
