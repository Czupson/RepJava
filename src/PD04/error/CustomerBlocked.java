package PD04.error;

import PD04.domain.CustomerStatus;

public record CustomerBlocked(int customerId, CustomerStatus status) implements RentalError {
    @Override
    public String message() {
        return "Customer with id " + customerId + " has status " + status + " and cannot rent";
    }
}
