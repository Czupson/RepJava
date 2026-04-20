package PD02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
record Money(BigDecimal amount, String currency) {
    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);

        if (currency.length() != 3)
            throw new IllegalArgumentException("currency must be 3 letters");

        currency = currency.toUpperCase();
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }
}

record Invoice(
        String number,                // e.g. "INV/2024/10/001"
        String clientName,
        Money netAmount,
        BigDecimal vatRate,           // 0.23 for 23%
        ZonedDateTime issuedAt,
        LocalDate dueDate             // payment deadline
) {
    public Invoice {
        Objects.requireNonNull(number);
        Objects.requireNonNull(clientName);
        Objects.requireNonNull(netAmount);
        Objects.requireNonNull(issuedAt);
        Objects.requireNonNull(dueDate);
        if (vatRate.signum() < 0 || vatRate.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("vatRate out of [0, 1]");
    }
}
