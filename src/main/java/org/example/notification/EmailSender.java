package org.example.notification;

public interface EmailSender {
    void send(String email, String message);
}