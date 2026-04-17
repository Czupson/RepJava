package PD02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InvoiceCalculator {
    private InvoiceCalculator() {}

    public static Money grossAmount(Invoice i) {

        BigDecimal multiplier = BigDecimal.ONE.add(i.vatRate());

        BigDecimal gross = i.netAmount().amount().multiply(multiplier)
                .setScale(2, RoundingMode.HALF_EVEN);
        return new Money(gross, i.netAmount().currency());
    }

    public static Money vatAmount(Invoice i) {
        BigDecimal vat = grossAmount(i).amount()
                .subtract(i.netAmount().amount())
                .setScale(2, RoundingMode.HALF_EVEN);
        return new Money(vat, i.netAmount().currency());
    }

    public static Money totalNet(Invoice... invoices) {
        if (invoices.length == 0) {
            return new Money(BigDecimal.ZERO, "PLN");
        }

        String currency = invoices[0].netAmount().currency();
        BigDecimal sum = BigDecimal.ZERO;

        for (Invoice i : invoices) {
            if (!currency.equals(i.netAmount().currency())) {
                throw new IllegalArgumentException("Mixed curriencies");
            }
            sum = sum.add(i.netAmount().amount());
        }
        return new Money(sum, currency);
    }

    public static String renderSimpleRaport(ZoneId displayZone, Invoice... invoice) {
        StringBuilder sb = new StringBuilder();

        sb.append("Invoice Raport (display zone: ").append(displayZone).append(")\n");
        sb.append("──────────────────────────────────────────────────\\n");
        for (Invoice i : invoice) {
            ZonedDateTime displayed = i.issuedAt().withZoneSameInstant(displayZone);

            sb.append(String.format(
                    " %-15s | %-12s | %s | net %10.2f %s | gross %10.2f %s%n",
                    i.number(),
                    i.clientName(),
                    displayed.toLocalDateTime(),
                    i.netAmount().amount(),
                    i.netAmount().currency(),
                    InvoiceCalculator.grossAmount(i).amount(),
                    i.netAmount().currency()
            ));
        }
        sb.append("──────────────────────────────────────────────────");
        return sb.toString();
    }



}
