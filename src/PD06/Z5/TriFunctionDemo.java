package PD06.Z5;

public class TriFunctionDemo {
    @FunctionalInterface
    interface TriFunction<T, U, V, R>{
        R apply(T t, U u, V v);
    }

    public static void main(String[] args) {
        TriFunction<Double, Double, Double, Double>
                weightedAverage = (ocena, waga, maxWaga) ->
                ocena * waga / maxWaga;
        double[] oceny = {3.0, 4.5, 5.0};
        double[] wagi = {1.0, 2.0, 3.0};
        double maxWaga = 0;

        for (double w : wagi) {
            maxWaga += w;
        }

        double result = 0;

        for (int i = 0; i < oceny.length; i++) {
            result += weightedAverage.apply(oceny[i], wagi[i], maxWaga);
        }
        System.out.println("Łączna średnia ważona: " +result);
    }
}
