package pl.futurecollars.invoicing.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class Invoice {

    private UUID id;
    private Date date;
    private Company issuer;
    private Company receiver;
    private List<InvoiceEntry> entries;

    public Invoice(Date date, Company issuer, Company receiver, List<InvoiceEntry> entries) {
        this.date = date;
        this.issuer = issuer;
        this.receiver = receiver;
        this.entries = entries;
    }
}

