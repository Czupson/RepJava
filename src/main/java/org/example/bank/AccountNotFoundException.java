package org.example.bank;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Konto nie znalezione: " + accountNumber);
    }
}
