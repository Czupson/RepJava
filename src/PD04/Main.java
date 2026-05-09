package PD04;

import PD04.catalog.Catalog;
import PD04.domain.*;
import PD04.error.RentalException;
import PD04.service.ErrorDescriber;
import PD04.service.RentalService;
import PD04.session.SessionLog;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Catalog<Movie> movies = new Catalog<>();
        movies.add(1, new Movie(1, "Shrek",        "Adamson",   2001, Category.FAMILY));
        movies.add(2, new Movie(2, "Pulp Fiction", "Tarantino", 1994, Category.DRAMA));
        movies.add(3, new Movie(3, "Matrix",       "Wachowski", 1999, Category.ACTION));

        Catalog<Customer> customers = new Catalog<>();
        customers.add(10, new Customer(10, "Anna",  "Nowak", 30, CustomerStatus.ACTIVE));
        customers.add(11, new Customer(11, "Tomek", "Maly",  10, CustomerStatus.ACTIVE));

        RentalService service = new RentalService(movies, customers);
        LocalDate today = LocalDate.of(2026, 5, 3);

        try (SessionLog log = new SessionLog()) {
            log.log("=== Movie Rental ===");

            tryRent(service, log, 10, 1, today); // Anna  -> Shrek            — OK
            tryRent(service, log, 11, 2, today); // Tomek -> Pulp Fiction     — TooYoungForCategory
            tryRent(service, log, 99, 3, today); // unknown customer          — CustomerNotFound

            int fine = service.returnMovie(1, today.plusDays(12)); // FAMILY: 7 dni → 5 dni zwloki
            log.log("Returning rentalId=1 (5 days late) — fine: " + fine + " PLN");
        }
    }

    private static void tryRent(RentalService service, SessionLog log,
                                int customerId, int movieId, LocalDate today) {
        try {
            Rental r = service.rent(customerId, movieId, today);
            log.log("Renting movieId=" + movieId + " -> customerId=" + customerId
                    + "  OK (return by " + r.plannedReturnDate() + ")");
        } catch (RentalException ex) {
            log.log("Renting movieId=" + movieId + " -> customerId=" + customerId
                    + "  ERROR: " + ErrorDescriber.describe(ex.error()));
        }
    }
}
