package PD02;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceCalculatorTest {

    private Invoice samplePLN() {
        return new Invoice(
                "INV/1",
                "Client A",
                new Money(new BigDecimal("100.00"), "PLN"),
                new BigDecimal("0.23"),
                ZonedDateTime.of(2026,4,15,10,0,0,0, ZoneId.of("Europe/Warsaw")),
                LocalDate.of(2026,4,30)
        );
    }

    private Invoice sampleEUR() {
        return new Invoice(
                "INV/2",
                "Client B",
                new Money(new BigDecimal("100.00"), "EUR"),
                new BigDecimal("0.23"),
                ZonedDateTime.of(2026,4,15,10,0,0,0, ZoneId.of("Europe/Warsaw")),
                LocalDate.of(2026,4,30)
        );
    }

    @Test
    void shouldCalculateGrossAmount() {
        Money gross = InvoiceCalculator.grossAmount(samplePLN());

        assertEquals(new BigDecimal("123.00"), gross.amount());
    }

    @Test
    void shouldCalculateVatAmount() {
        Money vat = InvoiceCalculator.vatAmount(samplePLN());

        assertEquals(new BigDecimal("23.00"), vat.amount());
    }

    @Test
    void shouldSumNetAmounts() {
        Money total = InvoiceCalculator.totalNet(samplePLN(), samplePLN());

        assertEquals(new BigDecimal("200.00"), total.amount());
    }

    @Test
    void shouldThrowOnMixedCurrencies() {
        assertThrows(IllegalArgumentException.class,
                () -> InvoiceCalculator.totalNet(samplePLN(), sampleEUR()));
    }

    @Test
    void shouldRenderSimpleReport() {
        String report = InvoiceCalculator.renderSimpleReport(
                ZoneId.of("Europe/Warsaw"),
                samplePLN()
        );

        assertTrue(report.contains("INV/1"));
        assertTrue(report.contains("Client A"));
        assertTrue(report.contains("PLN"));
    }

    @Test
    void shouldGroupMonthlyGrossInPln() {
        ExchangeRateTable rates = ExchangeRateTable.sampleRates();

        Map<YearMonth, Money> result =
                InvoiceCalculator.monthlyGrossInPln(
                        List.of(samplePLN()),
                        rates
                );

        YearMonth ym = YearMonth.of(2026,4);

        assertTrue(result.containsKey(ym));
        assertEquals("PLN", result.get(ym).currency());
    }

    @Test
    void shouldReturnZeroIfNotLate() {
        Money interest = InvoiceCalculator.lateInterest(
                samplePLN(),
                LocalDate.of(2026,4,30),
                new BigDecimal("0.115")
        );

        assertEquals(BigDecimal.ZERO.setScale(2), interest.amount());
    }

    @Test
    void shouldCalculateLateInterest() {
        Money interest = InvoiceCalculator.lateInterest(
                samplePLN(),
                LocalDate.of(2026,5,10), // late
                new BigDecimal("0.115")
        );

        assertTrue(interest.amount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void shouldSplitPaymentCorrectly() {
        SplitPaymentBreakdown split = InvoiceCalculator.split(samplePLN());

        assertEquals(new BigDecimal("100.00"), split.mainAccount().amount());
        assertEquals(new BigDecimal("23.00"), split.vatAccount().amount());
    }

    @Test
    void shouldApplyCorrections() {
        Invoice invoice = samplePLN();

        CorrectionInvoice correction = new CorrectionInvoice(
                "C1",
                "INV/1",
                new Money(new BigDecimal("10.00"), "PLN"),
                LocalDate.now(),
                "Adjustment"
        );

        Money result = InvoiceCalculator.effectiveGross(
                invoice,
                List.of(correction)
        );

        assertEquals(new BigDecimal("133.00"), result.amount());
    }

    @Test
    void shouldThrowOnCorrectionCurrencyMismatch() {
        Invoice invoice = samplePLN();

        CorrectionInvoice correction = new CorrectionInvoice(
                "C1",
                "INV/1",
                new Money(new BigDecimal("10.00"), "EUR"),
                LocalDate.now(),
                "Bad"
        );

        assertThrows(IllegalArgumentException.class,
                () -> InvoiceCalculator.effectiveGross(
                        invoice,
                        List.of(correction)
                ));
    }

    @Test
    void shouldRenderMonthlyReport() {
        ExchangeRateTable rates = ExchangeRateTable.sampleRates();

        String report = InvoiceCalculator.renderMonthlyReport(
                YearMonth.of(2026,4),
                List.of(samplePLN(), sampleEUR()),
                rates,
                ZoneId.of("Europe/Warsaw")
        );

        assertTrue(report.contains("Monthly Report"));
        assertTrue(report.contains("Total invoices: 2"));
        assertTrue(report.contains("Top 3 clients"));
    }
}

