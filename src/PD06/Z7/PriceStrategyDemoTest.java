package PD06.Z7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceStrategyDemoTest {

    @FunctionalInterface
    interface PriceStrategy {

        double apply(double basePrice);
    }

    static double calculatePrice(double basePrice,
                                 PriceStrategy strategy) {

        return strategy.apply(basePrice);
    }

    @Test
    void shouldReturnNormalPrice() {

        PriceStrategy normal = price -> price;

        double result = calculatePrice(100, normal);

        assertEquals(100, result);
    }

    @Test
    void shouldApplyStudentDiscount() {

        PriceStrategy student = price -> price * 0.9;

        double result = calculatePrice(100, student);

        assertEquals(90, result);
    }

    @Test
    void shouldApplyVipDiscount() {

        PriceStrategy vip = price -> price * 0.8;

        double result = calculatePrice(200, vip);

        assertEquals(160, result);
    }

    @Test
    void shouldApplyBlackFridayDiscount() {

        PriceStrategy blackFriday = price -> price * 0.7;

        double result = calculatePrice(300, blackFriday);

        assertEquals(210, result);
    }
}