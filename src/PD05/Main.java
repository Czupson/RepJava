package PD05;

public class Main {

    public static void main(String[] args) {

        TwoKeyMap<String, String, Integer> grades
                = new NestedTwoKeyHashMap<>();

        grades.put("Alice", "Math", 5);
        grades.put("Alice", "CS", 4);

        System.out.println(
                grades.get("Alice", "Math")
        );

        System.out.println(
                grades.row("Alice")
        );
    }
}
