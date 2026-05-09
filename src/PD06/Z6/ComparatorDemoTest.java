package PD06.Z6;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorDemoTest {

    static class Product {

        String name;
        double price;
        int rating;

        Product(String name, double price, int rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }
    }

    @Test
    void shouldSortByPriceAscending() {

        List<Product> products = new ArrayList<>();

        products.add(new Product("Laptop", 4500, 5));
        products.add(new Product("Mouse", 120, 4));
        products.add(new Product("Keyboard", 250, 5));

        products.sort(
                Comparator.comparing((Product p) -> p.price)
        );

        assertEquals("Mouse", products.get(0).name);
        assertEquals("Keyboard", products.get(1).name);
        assertEquals("Laptop", products.get(2).name);
    }

    @Test
    void shouldSortByPriceAndThenRatingDescending() {

        List<Product> products = new ArrayList<>();

        products.add(new Product("Headphones", 250, 4));
        products.add(new Product("Keyboard", 250, 5));
        products.add(new Product("Mouse", 120, 3));

        products.sort(
                Comparator.comparing((Product p) -> p.price)
                        .thenComparing(
                                Comparator.comparing(
                                        (Product p) -> p.rating
                                ).reversed()
                        )
        );

        assertEquals("Mouse", products.get(0).name);
        assertEquals("Keyboard", products.get(1).name);
        assertEquals("Headphones", products.get(2).name);
    }
}