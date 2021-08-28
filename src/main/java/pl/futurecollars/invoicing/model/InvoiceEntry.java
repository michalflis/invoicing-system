package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class InvoiceEntry {

    private String description;
    private BigDecimal price;
    private BigDecimal vatValue;
    private Vat vatRate;

    public InvoiceEntry(String description, BigDecimal price, Vat vatRate) {
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
        this.vatValue = price.multiply(vatRate.getRate());
    }
}
