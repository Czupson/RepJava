package PD04.error;

public record MovieAlreadyRented(int movieId, String title) implements RentalError {
    @Override
    public String message() {
        return "Movie " + title + " is already rented";
    }
}
