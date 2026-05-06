package PD04.error;

import PD04.domain.Category;

public record TooYoungForCategory(int customerAge, Category category, int minimumAge)
        implements RentalError {
    @Override
    public String message() {
        return "Customer is " + customerAge + " years old, category " +
                category + " requires " + minimumAge + "+";
    }
}
