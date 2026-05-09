package PD04.error;

public  record RentalLimitExceeded(int customerId, int active, int limit)
        implements RentalError {
    @Override
    public String message() {
        return "Customer with id " + customerId +
                " already has " + active +
                " active rentals (limit " + limit + ")";
    }
}
