package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

    private UUID id;
    private LocalDate date;
    private Company issuer;
    private Company receiver;
    private List<InvoiceEntry> entries;

    public Invoice(LocalDate date, Company issuer, Company receiver, List<InvoiceEntry> entries) {
        this.date = date;
        this.issuer = issuer;
        this.receiver = receiver;
        this.entries = entries;
    }
}

