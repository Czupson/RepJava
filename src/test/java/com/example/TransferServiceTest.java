package com.example;

import org.assertj.core.api.Assertions;
import org.example.bank.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransferService")
class TransferServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuditLogger auditLogger;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransferService transferService;

    private Account senderAccount;
    private Account receiverAccount;

    @BeforeEach
    void setUp() {
        senderAccount = new Account(
                "ACC-1",
                "Jan Kowalski",
                1000.0
        );

        receiverAccount = new Account(
                "ACC-2",
                "Anna Nowak",
                500.0
        );
    }

    @Nested
    @DisplayName("Udane przelewy")
    class SuccessfulTransferTests {

        @Test
        @DisplayName("Powinien wykonać poprawny przelew między kontami")
        void shouldTransferMoneyBetweenAccounts() {

            Mockito.when(accountRepository.findByAccountNumber("ACC-1"))
                    .thenReturn(Optional.of(senderAccount));

            Mockito.when(accountRepository.findByAccountNumber("ACC-2"))
                    .thenReturn(Optional.of(receiverAccount));

            Mockito.when(accountRepository.save(ArgumentMatchers.any(Account.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            transferService.transfer("ACC-1", "ACC-2", 200.0);

            assertThat(senderAccount.getBalance()).isEqualTo(800.0);
            assertThat(receiverAccount.getBalance()).isEqualTo(700.0);

            Mockito.verify(accountRepository, Mockito.times(2))
                    .save(ArgumentMatchers.any(Account.class));

            Mockito.verify(notificationService)
                    .sendTransferConfirmation(
                            "ACC-1",
                            200.0,
                            "ACC-2"
                    );

            Mockito.verify(notificationService, Mockito.never())
                    .sendTransferFailure(ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString());

            Mockito.verify(auditLogger).logTransfer(ArgumentMatchers.any(TransferLog.class));
        }
    }

    @Nested
    @DisplayName("Brak środków")
    class InsufficientFundsTests {

        @Test
        @DisplayName("Powinien rzucić wyjątek przy niewystarczających środkach")
        void shouldThrowExceptionWhenInsufficientFunds() {

            senderAccount.setBalance(100.0);

            Mockito.when(accountRepository.findByAccountNumber("ACC-1"))
                    .thenReturn(Optional.of(senderAccount));

            Mockito.when(accountRepository.findByAccountNumber("ACC-2"))
                    .thenReturn(Optional.of(receiverAccount));

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer("ACC-1", "ACC-2", 500.0))
                    .isInstanceOf(InsufficientFundsException.class)
                    .hasMessageContaining("ACC-1");

            Mockito.verify(accountRepository, Mockito.never())
                    .save(ArgumentMatchers.any(Account.class));

            Mockito.verify(auditLogger)
                    .logTransfer(ArgumentMatchers.any(TransferLog.class));

            Mockito.verify(notificationService)
                    .sendTransferFailure(
                            ArgumentMatchers.eq("ACC-1"),
                            ArgumentMatchers.eq(500.0),
                            ArgumentMatchers.anyString()
                    );
        }

        @Test
        @DisplayName("Nie powinien wysłać potwierdzenia sukcesu")
        void shouldNotSendSuccessNotificationWhenInsufficientFunds() {

            senderAccount.setBalance(100.0);

            Mockito.when(accountRepository.findByAccountNumber("ACC-1"))
                    .thenReturn(Optional.of(senderAccount));

            Mockito.when(accountRepository.findByAccountNumber("ACC-2"))
                    .thenReturn(Optional.of(receiverAccount));

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer("ACC-1", "ACC-2", 500.0))
                    .isInstanceOf(InsufficientFundsException.class);

            Mockito.verify(notificationService)
                    .sendTransferFailure(
                            ArgumentMatchers.eq("ACC-1"),
                            ArgumentMatchers.eq(500.0),
                            ArgumentMatchers.anyString()
                    );

            Mockito.verifyNoMoreInteractions(notificationService);
        }
    }

    @Nested
    @DisplayName("Brak konta")
    class AccountNotFoundTests {

        @Test
        @DisplayName("Powinien rzucić wyjątek gdy konto nadawcy nie istnieje")
        void shouldThrowExceptionWhenSenderAccountNotFound() {

            Mockito.when(accountRepository.findByAccountNumber("ACC-FAKE"))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer(
                            "ACC-FAKE",
                            "ACC-2",
                            100.0))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessageContaining("ACC-FAKE");

            Mockito.verifyNoInteractions(auditLogger, notificationService);
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek gdy konto odbiorcy nie istnieje")
        void shouldThrowExceptionWhenReceiverAccountNotFound() {

            Mockito.when(accountRepository.findByAccountNumber("ACC-1"))
                    .thenReturn(Optional.of(senderAccount));

            Mockito.when(accountRepository.findByAccountNumber("ACC-FAKE"))
                    .thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer(
                            "ACC-1",
                            "ACC-FAKE",
                            100.0))
                    .isInstanceOf(AccountNotFoundException.class)
                    .hasMessageContaining("ACC-FAKE");

            Mockito.verifyNoInteractions(auditLogger, notificationService);
        }
    }

    @Nested
    @DisplayName("Walidacja")
    class ValidationTests {

        @Test
        @DisplayName("Powinien rzucić wyjątek dla ujemnej kwoty")
        void shouldThrowExceptionForNegativeAmount() {

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer(
                            "ACC-1",
                            "ACC-2",
                            -100.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("większa od zera");

            Mockito.verifyNoInteractions(
                    accountRepository,
                    auditLogger,
                    notificationService
            );
        }

        @Test
        @DisplayName("Powinien rzucić wyjątek dla kwoty zero")
        void shouldThrowExceptionForZeroAmount() {

            Assertions.assertThatThrownBy(() ->
                    transferService.transfer(
                            "ACC-1",
                            "ACC-2",
                            0.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("większa od zera");

            Mockito.verifyNoInteractions(
                    accountRepository,
                    auditLogger,
                    notificationService
            );
        }
    }
}
