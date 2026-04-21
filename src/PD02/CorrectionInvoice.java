package PD02;

import java.time.LocalDate;

public record CorrectionInvoice(
        String correctionNumber,
        String originalNumber,
        Money amountAdjustment,
        LocalDate issuedOn,
        String reason
) {}