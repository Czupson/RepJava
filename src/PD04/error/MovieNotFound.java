package PD04.error;

public record MovieNotFound(int movieId) implements RentalError {
    @Override
    public String message() {
        return "Movie with id " + movieId + " does not exist";
    }
}
