package PD04.service;

import PD04.catalog.Catalog;
import PD04.domain.Customer;
import PD04.domain.CustomerStatus;
import PD04.domain.Movie;
import PD04.domain.Rental;
import PD04.error.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalService {
    public static final int ACTIVE_RENTALS_LIMIT = 3;
    public static final int OVERDUE_FINE_PER_DAY_PLN = 1;

    private final Catalog<Movie> movies;
    private final Catalog<Customer> customers;
    private final List<Rental> history = new ArrayList<>();
    private int nextRentalId = 1;

    public RentalService(Catalog<Movie> movies, Catalog<Customer> customers) {
        this.movies = movies;
        this.customers = customers;
    }
    public Rental rent(int customerId, int movieId, LocalDate today) throws RentalException{
        Optional<Customer> customerOpt = customers.find(customerId);
        if (customerOpt.isEmpty()) {
            throw new RentalException(new CustomerNotFound(customerId));
        }
        Customer customer = customerOpt.get();

        Optional<Movie> movieOpt = movies.find(movieId);
        if (movieOpt.isEmpty()) {
            throw new RentalException(new MovieNotFound(movieId));
        }
        Movie movie = movieOpt.get();
        
        if (customer.status() != CustomerStatus.ACTIVE){
            throw new RentalException(new CustomerBlocked(customerId, customer.status()));
        }

        if (customer.age() < movie.category().minimumAge()) {
            throw new RentalException(new TooYoungForCategory(
                    customer.age(),
                    movie.category(),
                    movie.category().minimumAge()
            ));
        }
        int active = activeRentalsForCustomer(customerId);
        if (active >= ACTIVE_RENTALS_LIMIT) {
            throw new RentalException(new RentalLimitExceeded(
                    customerId,
                    active,
                    ACTIVE_RENTALS_LIMIT
            ));
        }
        
        if (isMovieRented(movieId)) {
            throw new RentalException(new MovieAlreadyRented(
                    movieId,
                    movie.title()
            ));
        }

        if (today == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        Rental rental = new Rental(
                nextRentalId++,
                movieId,
                customerId,
                today,
                today.plusDays(movie.category().rentalDays()),
                Optional.empty()
        );
        history.add(rental);
        return rental;
    }
    public int returnMovie(int rentalId, LocalDate today) {
        for (int i = 0; i < history.size(); i++) {
            Rental rental = history.get(i);
            if(rental.id() == rentalId){
                if(rental.actualReturnDate().isPresent()){
                    throw new RentalStateException("Rental " + rentalId + " already returned");
                }
                Rental update = rental.withReturn(today);
                history.set(i, update);
                long daysLate = ChronoUnit.DAYS.between(
                        rental.plannedReturnDate(),
                        today
                );
                return Math.max(0,(int)daysLate) * OVERDUE_FINE_PER_DAY_PLN;
            }
        }
        throw new RentalStateException("Rental " + rentalId + " not found");
    }
    public int activeRentalsForCustomer(int customerId) {
        int count = 0;

        for (Rental rental : history) {
            if (rental.customerId() == customerId &&
            rental.actualReturnDate().isEmpty()) {
                count++;
            }
        }
        return count;
    }
    public boolean isMovieRented(int movieId) {
        for (Rental rental : history) {
            if (rental.movieId() == movieId &&
            rental.actualReturnDate().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
