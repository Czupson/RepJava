package org.example.notification;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Użytkownik nie istnieje: " + userId);
    }
}