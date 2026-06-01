package org.example.validation;

public class UserValidator {
    public void validateEmail(String email){

        if (email == null) {
            throw new NullPointerException("Email nie może być null");
        }

        if (email.isEmpty()) {
            throw new IllegalArgumentException("pusty");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("@");
        }
    }
}
