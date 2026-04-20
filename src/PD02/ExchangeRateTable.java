package PD02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record ExchangeRateTable(
        Map<LocalDate, Map<String, BigDecimal>> ratesByDate
) {

    public ExchangeRateTable {
        Objects.requireNonNull(ratesByDate);

        Map<LocalDate, Map<String, BigDecimal>> copy = new HashMap<>();

        for (var entry : ratesByDate.entrySet()) {
            copy.put(
                    entry.getKey(),
                    Map.copyOf(entry.getValue())
            );
        }

        ratesByDate = Map.copyOf(copy);
    }

    public BigDecimal rateOf(String currency, LocalDate onDate) {
        Objects.requireNonNull(currency);
        Objects.requireNonNull(onDate);

        currency = currency.toUpperCase();

        LocalDate bestDate = null;

        for (LocalDate date : ratesByDate.keySet()) {
            if (!date.isAfter(onDate)) {
                if (bestDate == null || date.isAfter(bestDate)) {
                    bestDate = date;
                }
            }
        }

        if (bestDate == null) {
            throw new IllegalStateException(
                    "no rate for " + currency + " on or before " + onDate
            );
        }

        BigDecimal rate = ratesByDate.get(bestDate).get(currency);

        if (rate == null) {
            throw new IllegalStateException(
                    "no rate for " + currency + " on or before " + onDate
            );
        }

        return rate;
    }

    public Money convertTo(
            Money source,
            String targetCurrency,
            LocalDate onDate
    ) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(targetCurrency);
        Objects.requireNonNull(onDate);

        targetCurrency = targetCurrency.toUpperCase();

        if (source.currency().equals(targetCurrency)) {
            return source;
        }

        BigDecimal sourceRate = rateOf(source.currency(), onDate);
        BigDecimal targetRate = rateOf(targetCurrency, onDate);

        BigDecimal plnValue = source.amount()
                .multiply(sourceRate);

        BigDecimal targetAmount = plnValue.divide(
                targetRate,
                2,
                RoundingMode.HALF_EVEN
        );

        return new Money(targetAmount, targetCurrency);
    }

    public static ExchangeRateTable sampleRates() {
        Map<LocalDate, Map<String, BigDecimal>> m = new HashMap<>();

        m.put(LocalDate.of(2024, 10, 15), Map.of(
                "PLN", new BigDecimal("1.0000"),
                "EUR", new BigDecimal("4.3250"),
                "USD", new BigDecimal("3.9800"),
                "JPY", new BigDecimal("0.0265")
        ));

        m.put(LocalDate.of(2024, 10, 16), Map.of(
                "PLN", new BigDecimal("1.0000"),
                "EUR", new BigDecimal("4.3310"),
                "USD", new BigDecimal("3.9875"),
                "JPY", new BigDecimal("0.0264")
        ));

        m.put(LocalDate.of(2024, 10, 17), Map.of(
                "PLN", new BigDecimal("1.0000"),
                "EUR", new BigDecimal("4.3400"),
                "USD", new BigDecimal("4.0010"),
                "JPY", new BigDecimal("0.0267")
        ));

        return new ExchangeRateTable(m);
    }
}

