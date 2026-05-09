package PD04.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void shouldCreateValidMovie() {
        Movie movie = new Movie(1, "Shrek", "Adamson", 2001, Category.FAMILY);

        assertEquals(1, movie.id());
        assertEquals("Shrek", movie.title());
        assertEquals("Adamson", movie.director());
        assertEquals(2001, movie.year());
        assertEquals(Category.FAMILY, movie.category());
    }

    @Test
    void shouldThrowWhenTitleIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, null, "Dir", 2000, Category.FAMILY));
    }

    @Test
    void shouldThrowWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, "   ", "Dir", 2000, Category.FAMILY));
    }

    @Test
    void shouldThrowWhenDirectorIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, "Title", null, 2000, Category.FAMILY));
    }

    @Test
    void shouldThrowWhenDirectorIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, "Title", "   ", 2000, Category.FAMILY));
    }

    @Test
    void shouldThrowWhenCategoryIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, "Title", "Dir", 2000, null));
    }

    @Test
    void shouldThrowWhenYearTooSmall() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1, "Title", "Dir", 1800, Category.FAMILY));
    }
}