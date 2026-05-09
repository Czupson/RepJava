package PD04.catalog;

import PD04.domain.Category;
import PD04.domain.Movie;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    @Test
    void shouldAddAndFindElement() {
        Catalog<Movie> catalog = new Catalog<>();
        Movie movie = new Movie(1, "Shrek", "Adamson", 2001, Category.FAMILY);

        catalog.add(1, movie);

        Optional<Movie> result = catalog.find(1);

        assertTrue(result.isPresent());
        assertEquals(movie, result.get());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Catalog<Movie> catalog = new Catalog<>();

        Optional<Movie> result = catalog.find(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldContainIdWhenAdded() {
        Catalog<Movie> catalog = new Catalog<>();

        catalog.add(1, new Movie(1, "Shrek", "Adamson", 2001, Category.FAMILY));

        assertTrue(catalog.contains(1));
    }

    @Test
    void shouldNotContainIdWhenNotAdded() {
        Catalog<Movie> catalog = new Catalog<>();

        assertFalse(catalog.contains(1));
    }

    @Test
    void shouldReturnCorrectSize() {
        Catalog<Movie> catalog = new Catalog<>();

        catalog.add(1, new Movie(1, "A", "Dir", 2000, Category.FAMILY));
        catalog.add(2, new Movie(2, "B", "Dir", 2001, Category.ACTION));

        assertEquals(2, catalog.size());
    }

    @Test
    void shouldThrowWhenAddingNullElement() {
        Catalog<Movie> catalog = new Catalog<>();

        assertThrows(IllegalArgumentException.class,
                () -> catalog.add(1, null));
    }

    @Test
    void shouldThrowWhenAddingDuplicateId() {
        Catalog<Movie> catalog = new Catalog<>();

        catalog.add(1, new Movie(1, "A", "Dir", 2000, Category.FAMILY));

        assertThrows(IllegalArgumentException.class,
                () -> catalog.add(1, new Movie(1, "B", "Dir", 2001, Category.ACTION)));
    }
}