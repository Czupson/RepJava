package PD04.service;

import PD04.catalog.Catalog;
import PD04.domain.Category;
import PD04.domain.Customer;
import PD04.domain.CustomerStatus;
import PD04.domain.Movie;
import PD04.error.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private RentalService service;
    private LocalDate today;

    @BeforeEach
    void setup() {
        Catalog<Movie> movies = new Catalog<>();
        movies.add(1, new Movie(1, "Shrek", "Adamson", 2001, Category.FAMILY));
        movies.add(2, new Movie(2, "Matrix", "Wachowski", 1999, Category.ACTION));
        movies.add(3, new Movie(3, "Saw", "Wan", 2004, Category.HORROR));

        Catalog<Customer> customers = new Catalog<>();
        customers.add(1, new Customer(1, "Jan", "Kowalski", 25, CustomerStatus.ACTIVE));
        customers.add(2, new Customer(2, "Ola", "Nowak", 10, CustomerStatus.ACTIVE));
        customers.add(3, new Customer(3, "Blocked", "User", 30, CustomerStatus.BLOCKED));

        service = new RentalService(movies, customers);
        today = LocalDate.now();
    }

    @Test
    void shouldThrowCustomerNotFound() {
        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(999, 1, today));

        assertTrue(ex.error() instanceof CustomerNotFound);
    }

    @Test
    void shouldThrowMovieNotFound() {
        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(1, 999, today));

        assertTrue(ex.error() instanceof MovieNotFound);
    }

    @Test
    void shouldThrowCustomerBlocked() {
        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(3, 1, today));

        assertTrue(ex.error() instanceof CustomerBlocked);
    }

    @Test
    void shouldThrowTooYoung() {
        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(2, 3, today));

        assertTrue(ex.error() instanceof TooYoungForCategory t);
        TooYoungForCategory t = (TooYoungForCategory) ex.error();
        assertEquals(10, t.customerAge());
    }

    @Test
    void shouldThrowMovieAlreadyRented() throws RentalException {
        service.rent(1, 1, today);

        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(2, 1, today));

        assertTrue(ex.error() instanceof MovieAlreadyRented);
    }

    @Test
    void shouldThrowRentalLimitExceeded() throws RentalException {
        service.rent(1, 1, today);
        service.rent(1, 2, today);
        service.rent(1, 3, today);

        RentalException ex = assertThrows(RentalException.class,
                () -> service.rent(1, 1, today.plusDays(1)));

        assertTrue(ex.error() instanceof RentalLimitExceeded);
    }
}