package PD04.error;

public class RentalException extends Exception {
    private final RentalError error;

    public RentalException(RentalError error) {
        super(error.message());
        this.error = error;
    }
    public RentalError error() {
        return error;
    }
}
