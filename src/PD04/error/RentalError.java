package PD04.error;

public sealed interface RentalError
    permits MovieNotFound, CustomerNotFound, CustomerBlocked,
            TooYoungForCategory, MovieAlreadyRented, RentalLimitExceeded {

        String message();
}
