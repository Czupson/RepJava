package org.example.bank;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByAccountNumber(String accountNumber);
    Account save(Account account);
}