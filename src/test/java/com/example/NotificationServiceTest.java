package com.example;

import org.example.notification.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSender emailSender;

    @Mock
    private SmsSender smsSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSendEmailAndSms_whenUserHasEmailAndPhoneNumber() {
        User user = new User(
                1L,
                "jan@example.com",
                "123456789"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        notificationService.notifyUser(1L, "Hello");

        verify(emailSender).send("jan@example.com", "Hello");
        verify(smsSender).send("123456789", "Hello");
    }

    @Test
    void shouldSendOnlyEmail_whenUserHasNoPhoneNumber() {
        User user = new User(
                1L,
                "jan@example.com",
                null
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        notificationService.notifyUser(1L, "Hello");

        verify(emailSender).send("jan@example.com", "Hello");
        verify(smsSender, never())
                .send(anyString(), anyString());
    }

    @Test
    void shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> notificationService.notifyUser(1L, "Hello")
        );

        verify(emailSender, never())
                .send(anyString(), anyString());

        verify(smsSender, never())
                .send(anyString(), anyString());
    }

    @Test
    void shouldSendCorrectMessageContent() {
        User user = new User(
                1L,
                "jan@example.com",
                null
        );

        String message = "Important notification";

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ArgumentCaptor<String> messageCaptor =
                ArgumentCaptor.forClass(String.class);

        notificationService.notifyUser(1L, message);

        verify(emailSender)
                .send(eq("jan@example.com"), messageCaptor.capture());

        assertEquals(message, messageCaptor.getValue());
    }
}
