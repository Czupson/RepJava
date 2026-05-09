package PD04.domain;

public record Customer(
        int id,
        String firstName,
        String lastName,
        int age,
        CustomerStatus status
) {
    public Customer {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("first name cannot be blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("last name cannot be blank");
        }
        if (age < 0 || age > 130) {
            throw new IllegalArgumentException("age must be between 0 and 130");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
    }
}
