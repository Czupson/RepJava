package PD06.Z7;

public class PriceStrategyDemo {
    @FunctionalInterface
    interface PriceStrategy {
        double apply(double bestPrice);
    }
    static double calculatePrice(double basePrice, PriceStrategy strategy) {
        return strategy.apply(basePrice);
    }
    public static void main(String[] args) {
        PriceStrategy normal = price -> price;
        PriceStrategy student = price -> price * 0.9;
        PriceStrategy vip = price -> price * 0.8;
        PriceStrategy blackFriday = price -> price * 0.7;

        double[] prices = {100, 250, 399};

        for (double price : prices) {
            System.out.println("\nBase price: " + price);

            System.out.println(
                    "Normal: "
                            + calculatePrice(price, normal)
            );

            System.out.println(
                    "Student: "
                            + calculatePrice(price, student)
            );

            System.out.println(
                    "VIP: "
                            + calculatePrice(price, vip)
            );

            System.out.println(
                    "Black Friday: "
                            + calculatePrice(price, blackFriday)
            );
        }
    }

}
