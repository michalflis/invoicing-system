package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue
    private UUID id;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-09-30")
    private LocalDate date;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "issuer")
    private Company issuer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "receiver")
    private Company receiver;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceEntry> entries;

    public Invoice(LocalDate date, Company issuer, Company receiver, List<InvoiceEntry> entries) {
        this.date = date;
        this.issuer = issuer;
        this.receiver = receiver;
        this.entries = entries;
    }
}

