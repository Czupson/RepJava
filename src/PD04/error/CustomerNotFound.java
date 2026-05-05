package PD04.error;

import PD04.domain.Rental;


public record CustomerNotFound(int customerId) implements RentalError {
    @Override
    public String message() {
        return "Customer with id " + customerId + " does not exist";
    }
}
