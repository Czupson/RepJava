package PD04.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCreateValidCustomer() {
        Customer c = new Customer(1, "Jan", "Kowalski", 25, CustomerStatus.ACTIVE);

        assertEquals(1, c.id());
        assertEquals("Jan", c.firstName());
        assertEquals("Kowalski", c.lastName());
        assertEquals(25, c.age());
        assertEquals(CustomerStatus.ACTIVE, c.status());
    }

    @Test
    void shouldThrowWhenFirstNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, null, "Kowalski", 25, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenFirstNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "   ", "Kowalski", 25, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenLastNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "Jan", null, 25, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenLastNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "Jan", "   ", 25, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenAgeNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "Jan", "Kowalski", -1, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenAgeTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "Jan", "Kowalski", 131, CustomerStatus.ACTIVE));
    }

    @Test
    void shouldThrowWhenStatusIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(1, "Jan", "Kowalski", 25, null));
    }
}