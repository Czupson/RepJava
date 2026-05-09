package PD06.Z6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {
    static class Product {
        String name;
        double price;
        int rating;

        public Product(String name, double price, int rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }
        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    ", rating=" + rating +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Product> products = new ArrayList<Product>();

        products.add(new Product("Laptop", 4500, 5));
        products.add(new Product("Mouse", 120, 4));
        products.add(new Product("Keyboard", 250, 5));
        products.add(new Product("Monitor", 1200, 3));
        products.add(new Product("Headphones", 250, 4));

        products.sort(
                Comparator.comparing(
                        (Product p) -> p.price
                ).thenComparing(
                        Comparator.comparing(
                                (Product p) -> p.rating
                        ).reversed()
                )
        );
        System.out.println("Sorted products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
