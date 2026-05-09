package PD04.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void kidsCategoryShouldHaveCorrectValues() {
        Category c = Category.KIDS;

        assertEquals(14, c.rentalDays());
        assertEquals(3, c.pricePLN());
        assertEquals(0, c.minimumAge());
    }

    @Test
    void familyCategoryShouldHaveCorrectValues() {
        Category c = Category.FAMILY;

        assertEquals(7, c.rentalDays());
        assertEquals(5, c.pricePLN());
        assertEquals(0, c.minimumAge());
    }

    @Test
    void dramaCategoryShouldHaveCorrectValues() {
        Category c = Category.DRAMA;

        assertEquals(7, c.rentalDays());
        assertEquals(6, c.pricePLN());
        assertEquals(12, c.minimumAge());
    }

    @Test
    void actionCategoryShouldHaveCorrectValues() {
        Category c = Category.ACTION;

        assertEquals(5, c.rentalDays());
        assertEquals(7, c.pricePLN());
        assertEquals(16, c.minimumAge());
    }

    @Test
    void horrorCategoryShouldHaveCorrectValues() {
        Category c = Category.HORROR;

        assertEquals(5, c.rentalDays());
        assertEquals(8, c.pricePLN());
        assertEquals(18, c.minimumAge());
    }
}