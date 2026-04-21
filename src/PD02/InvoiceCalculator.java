package PD02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class InvoiceCalculator {

    private InvoiceCalculator() {}

    public static Money grossAmount(Invoice i) {
        BigDecimal multiplier = BigDecimal.ONE.add(i.vatRate());

        BigDecimal gross = i.netAmount().amount()
                .multiply(multiplier)
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
                throw new IllegalArgumentException("Mixed currencies");
            }
            sum = sum.add(i.netAmount().amount());
        }

        return new Money(sum, currency);
    }

    public static String renderSimpleReport(ZoneId displayZone, Invoice... invoices) {
        StringBuilder sb = new StringBuilder();

        sb.append("Invoice Report (display zone: ")
                .append(displayZone)
                .append(")\n");

        sb.append("──────────────────────────────────────────────────\n");

        for (Invoice i : invoices) {
            ZonedDateTime displayed = i.issuedAt()
                    .withZoneSameInstant(displayZone);

            String date = displayed.toLocalDateTime()
                    .toString()
                    .replace("T", " ");

            sb.append(String.format(
                    " %-15s | %-12s | %s | net %10.2f %s | gross %10.2f %s%n",
                    i.number(),
                    i.clientName(),
                    date,
                    i.netAmount().amount(),
                    i.netAmount().currency(),
                    grossAmount(i).amount(),
                    i.netAmount().currency()
            ));
        }

        sb.append("──────────────────────────────────────────────────");

        return sb.toString();
    }

    public static Map<YearMonth, Money> monthlyGrossInPln(
            List<Invoice> invoices,
            ExchangeRateTable rates
    ) {
        final ExchangeRateTable fx = rates;

        return invoices.stream()
                .collect(Collectors.groupingBy(
                        i -> YearMonth.from(i.issuedAt()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal sum = list.stream()
                                            .map(i -> fx.convertTo(
                                                    grossAmount(i),
                                                    "PLN",
                                                    i.issuedAt().toLocalDate()
                                            ))
                                            .map(Money::amount)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    return new Money(sum, "PLN");
                                }
                        )
                ));
    }

    public static Money lateInterest(
            Invoice invoice,
            LocalDate paidOn,
            BigDecimal annualRate
    ) {
        if (!paidOn.isAfter(invoice.dueDate())) {
            return new Money(BigDecimal.ZERO, invoice.netAmount().currency());
        }

        long daysLate = ChronoUnit.DAYS.between(
                invoice.dueDate(),
                paidOn
        );

        BigDecimal interest = grossAmount(invoice).amount()
                .multiply(annualRate)
                .multiply(BigDecimal.valueOf(daysLate))
                .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_EVEN);

        return new Money(interest, invoice.netAmount().currency());
    }

    public static String renderMonthlyReport(
            YearMonth month,
            List<Invoice> invoices,
            ExchangeRateTable rates,
            ZoneId displayZone
    ) {
        final ExchangeRateTable fx = rates;

        List<Invoice> filtered = invoices.stream()
                .filter(i -> YearMonth.from(i.issuedAt()).equals(month))
                .toList();

        int invoiceCount = filtered.size();

        BigDecimal totalGrossPln = filtered.stream()
                .map(i -> fx.convertTo(
                        grossAmount(i),
                        "PLN",
                        i.issuedAt().toLocalDate()
                ).amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, List<Invoice>> byCurrency = filtered.stream()
                .collect(Collectors.groupingBy(i -> i.netAmount().currency()));

        StringBuilder currencySection = new StringBuilder();

        for (var entry : byCurrency.entrySet()) {
            final String currency = entry.getKey();
            List<Invoice> list = entry.getValue();

            BigDecimal sum = list.stream()
                    .map(i -> grossAmount(i).amount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            currencySection.append(String.format(
                    "║   %s: %d invoices, %,.2f %s                   ║%n",
                    currency,
                    list.size(),
                    sum,
                    currency
            ));
        }

        Map<String, BigDecimal> revenueByClient =
                filtered.stream()
                        .collect(Collectors.groupingBy(
                                Invoice::clientName,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        i -> fx.convertTo(
                                                grossAmount(i),
                                                "PLN",
                                                i.issuedAt().toLocalDate()
                                        ).amount(),
                                        BigDecimal::add
                                )
                        ));

        List<Map.Entry<String, BigDecimal>> topClients =
                revenueByClient.entrySet().stream()
                        .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                        .limit(3)
                        .toList();

        StringBuilder topSection = new StringBuilder();

        int rank = 1;
        for (var entry : topClients) {
            String name = dotFill(entry.getKey(), 25);

            topSection.append(String.format(
                    "║  %d. %s %10.2f PLN        ║%n",
                    rank++,
                    name,
                    entry.getValue()
            ));
        }

        return """
╔══════════════════════════════════════════════════╗
║ Monthly Report: %s (zone %s)                    ║
╠══════════════════════════════════════════════════╣
║ Total invoices: %d                              ║
║ Total gross in PLN: %,.2f                       ║
╠══════════════════════════════════════════════════╣
║ Original currencies:                           ║
%s╠══════════════════════════════════════════════════╣
║ Top 3 clients (by gross in PLN):               ║
%s╚══════════════════════════════════════════════════╝
""".formatted(
                month,
                displayZone,
                invoiceCount,
                totalGrossPln,
                currencySection.toString(),
                topSection.toString()
        );
    }

    public static SplitPaymentBreakdown split(Invoice i) {
        return new SplitPaymentBreakdown(
                i.netAmount(),
                vatAmount(i)
        );
    }

    public static Money effectiveGross(
            Invoice original,
            List<CorrectionInvoice> corrections
    ) {
        BigDecimal gross = grossAmount(original).amount();

        for (CorrectionInvoice c : corrections) {
            if (!c.amountAdjustment().currency()
                    .equals(original.netAmount().currency())) {
                throw new IllegalArgumentException("Currency mismatch");
            }

            gross = gross.add(c.amountAdjustment().amount());
        }

        return new Money(gross, original.netAmount().currency());
    }

    private static String dotFill(String text, int width) {
        return String.format("%-" + width + "s", text)
                .replace(' ', '.');
    }

    private static List<Invoice> invoices() {
        return List.of(
                new Invoice("INV/2026/04/001","Acme Corp",
                        new Money(new BigDecimal("1000.00"),"PLN"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,15,14,30,0,0,ZoneId.of("Europe/Warsaw")),
                        LocalDate.of(2026,4,30)),

                new Invoice("INV/2026/04/002","Globex",
                        new Money(new BigDecimal("100.37"),"EUR"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,16,9,15,0,0,ZoneId.of("Europe/Warsaw")),
                        LocalDate.of(2026,4,30)),

                new Invoice("INV/2026/04/003","Initech",
                        new Money(new BigDecimal("800.00"),"USD"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,17,16,0,0,0,ZoneId.of("America/New_York")),
                        LocalDate.of(2026,5,5)),

                new Invoice("INV/2026/04/004","Umbrella Corp",
                        new Money(new BigDecimal("150000"),"JPY"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,15,10,0,0,0,ZoneId.of("Asia/Tokyo")),
                        LocalDate.of(2026,4,28)),

                new Invoice("INV/2026/04/005","Acme Corp",
                        new Money(new BigDecimal("2500.00"),"PLN"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,16,12,45,0,0,ZoneId.of("Europe/Warsaw")),
                        LocalDate.of(2026,4,29)),

                new Invoice("INV/2026/04/006","Globex",
                        new Money(new BigDecimal("500.00"),"EUR"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,17,8,0,0,0,ZoneId.of("Europe/Warsaw")),
                        LocalDate.of(2026,5,2)),

                new Invoice("INV/2026/04/007","Soylent Corp",
                        new Money(new BigDecimal("1200.00"),"USD"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,16,18,0,0,0,ZoneId.of("America/Los_Angeles")),
                        LocalDate.of(2026,5,3)),

                new Invoice("INV/2026/04/008","Initech",
                        new Money(new BigDecimal("300.00"),"USD"),
                        new BigDecimal("0.23"),
                        ZonedDateTime.of(2026,4,15,11,30,0,0,ZoneId.of("America/New_York")),
                        LocalDate.of(2026,4,27))
        );
    }



    public static void main(String[] args) {
        YearMonth month = YearMonth.now();
        String client = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--month" -> month = YearMonth.parse(args[++i]);
                case "--client" -> client = args[++i];
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }

        final String clientFilter = client;

        List<Invoice> allInvoices = invoices();
        ExchangeRateTable rates = ExchangeRateTable.sampleRates();

        final List<Invoice> filteredInvoices =
                (clientFilter != null)
                        ? allInvoices.stream()
                        .filter(i -> i.clientName().equals(clientFilter))
                        .toList()
                        : allInvoices;

        String report = renderMonthlyReport(
                month,
                filteredInvoices,
                rates,
                ZoneId.of("Europe/Warsaw")
        );

        System.out.println(report);
    }
}
