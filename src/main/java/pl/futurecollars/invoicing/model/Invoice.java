package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

    @ApiModelProperty(hidden = true)
    private UUID id;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-09-30")
    private LocalDate date;

    private Company issuer;
    private Company receiver;
    private List<InvoiceEntry> entries;

    public Invoice(LocalDate date, Company issuer, Company receiver, List<InvoiceEntry> entries) {
        this.date = date;
        this.issuer = issuer;
        this.receiver = receiver;
        this.entries = entries;
        this.id = null;
    }
}

