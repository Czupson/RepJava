package PD04.domain;

import java.time.LocalDate;
import java.util.Optional;

public record Rental(
        int id,
        int movieId,
        int customerId,
        LocalDate rentDate,
        LocalDate plannedReturnDate,
        Optional<LocalDate> actualReturnDate
) {
    public Rental {
        if (rentDate == null) {
            throw new IllegalArgumentException("Rent date cannot be null");
        }
        if (plannedReturnDate == null) {
            throw new IllegalArgumentException("Planned return date cannot be null");
        }
        if (actualReturnDate == null) {
            throw new IllegalArgumentException("Actual return date cannot be null");
        }
        if (plannedReturnDate.isBefore(rentDate)) {
            throw new IllegalArgumentException("Planned return date cannot be before rent date");
        }
    }
    public Rental withReturn(LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("Return date cannot be null");
        }

        return new Rental(
                id,
                movieId,
                customerId,
                rentDate,
                plannedReturnDate,
                Optional.of(today)
        );
    }
}
