package PD04.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RentalTest {

    @Test
    void shouldCreateValidRental() {
        LocalDate today = LocalDate.now();

        Rental rental = new Rental(
                1,
                10,
                20,
                today,
                today.plusDays(5),
                Optional.empty()
        );

        assertEquals(1, rental.id());
        assertEquals(10, rental.movieId());
        assertEquals(20, rental.customerId());
        assertEquals(today, rental.rentDate());
        assertEquals(today.plusDays(5), rental.plannedReturnDate());
        assertTrue(rental.actualReturnDate().isEmpty());
    }

    @Test
    void shouldThrowWhenRentDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Rental(1, 1, 1, null,
                        LocalDate.now(), Optional.empty()));
    }

    @Test
    void shouldThrowWhenPlannedReturnDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Rental(1, 1, 1,
                        LocalDate.now(), null, Optional.empty()));
    }

    @Test
    void shouldThrowWhenActualReturnDateIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Rental(1, 1, 1,
                        LocalDate.now(), LocalDate.now(), null));
    }

    @Test
    void shouldThrowWhenPlannedBeforeRentDate() {
        LocalDate today = LocalDate.now();

        assertThrows(IllegalArgumentException.class,
                () -> new Rental(1, 1, 1,
                        today,
                        today.minusDays(1),
                        Optional.empty()));
    }

    @Test
    void shouldReturnNewRentalWithReturnDate() {
        LocalDate today = LocalDate.now();

        Rental original = new Rental(
                1,
                1,
                1,
                today,
                today.plusDays(5),
                Optional.empty()
        );

        Rental returned = original.withReturn(today.plusDays(3));

        assertNotSame(original, returned);

        assertTrue(original.actualReturnDate().isEmpty());

        assertTrue(returned.actualReturnDate().isPresent());
        assertEquals(today.plusDays(3), returned.actualReturnDate().get());
    }

    @Test
    void shouldThrowWhenReturnDateIsNull() {
        Rental rental = new Rental(
                1,
                1,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                Optional.empty()
        );

        assertThrows(IllegalArgumentException.class,
                () -> rental.withReturn(null));
    }
}