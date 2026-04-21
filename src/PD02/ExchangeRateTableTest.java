package PD02;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateTableTest {

    private ExchangeRateTable table = ExchangeRateTable.sampleRates();

    @Test
    void shouldReturnExactRateForDate() {
        BigDecimal rate = table.rateOf("EUR", LocalDate.of(2024, 10, 15));

        assertEquals(new BigDecimal("4.3250"), rate);
    }

    @Test
    void shouldReturnPreviousRateIfDateMissing() {
        BigDecimal rate = table.rateOf("EUR", LocalDate.of(2024, 10, 18));

        assertEquals(new BigDecimal("4.3400"), rate);
    }

    @Test
    void shouldBeCaseInsensitiveCurrency() {
        BigDecimal rate = table.rateOf("eur", LocalDate.of(2024, 10, 15));

        assertEquals(new BigDecimal("4.3250"), rate);
    }

    @Test
    void shouldThrowIfNoEarlierDate() {
        assertThrows(IllegalStateException.class,
                () -> table.rateOf("EUR", LocalDate.of(2024, 10, 10)));
    }

    @Test
    void shouldThrowIfCurrencyNotFound() {
        assertThrows(IllegalStateException.class,
                () -> table.rateOf("GBP", LocalDate.of(2024, 10, 15)));
    }

    @Test
    void shouldConvertCurrency() {
        Money eur = new Money(new BigDecimal("100.00"), "EUR");

        Money pln = table.convertTo(
                eur,
                "PLN",
                LocalDate.of(2024, 10, 15)
        );

        assertEquals(new BigDecimal("432.50"), pln.amount());
        assertEquals("PLN", pln.currency());
    }

    @Test
    void shouldConvertBetweenNonPlnCurrencies() {
        Money usd = new Money(new BigDecimal("100.00"), "USD");

        Money eur = table.convertTo(
                usd,
                "EUR",
                LocalDate.of(2024, 10, 15)
        );


        assertEquals(new BigDecimal("92.02"), eur.amount());
        assertEquals("EUR", eur.currency());
    }

    @Test
    void shouldReturnSameMoneyIfSameCurrency() {
        Money pln = new Money(new BigDecimal("100.00"), "PLN");

        Money result = table.convertTo(
                pln,
                "PLN",
                LocalDate.of(2024, 10, 15)
        );

        assertEquals(pln, result);
    }

    @Test
    void shouldUsePreviousRateForConversion() {
        Money eur = new Money(new BigDecimal("100.00"), "EUR");

        Money pln = table.convertTo(
                eur,
                "PLN",
                LocalDate.of(2024, 10, 18)
        );

        // używa 2024-10-17 → 4.3400
        assertEquals(new BigDecimal("434.00"), pln.amount());
    }

    @Test
    void shouldThrowOnMissingRate() {
        Money gbp = new Money(new BigDecimal("100.00"), "GBP");

        assertThrows(IllegalStateException.class,
                () -> table.convertTo(
                        gbp,
                        "PLN",
                        LocalDate.of(2024, 10, 15)
                ));
    }

    @Test
    void shouldThrowOnNullArguments() {
        assertThrows(NullPointerException.class,
                () -> table.convertTo(null, "PLN", LocalDate.now()));

        assertThrows(NullPointerException.class,
                () -> table.convertTo(
                        new Money(new BigDecimal("10"), "PLN"),
                        null,
                        LocalDate.now()
                ));

        assertThrows(NullPointerException.class,
                () -> table.convertTo(
                        new Money(new BigDecimal("10"), "PLN"),
                        "EUR",
                        null
                ));
    }
}

