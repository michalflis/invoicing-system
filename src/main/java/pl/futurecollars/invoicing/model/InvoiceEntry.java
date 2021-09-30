package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceEntry {

    @ApiModelProperty(value = "Product description", required = true, example = "Młotek")
    private String description;

    @ApiModelProperty(value = "Product price", required = true, example = "12.05")
    private BigDecimal price;

    @ApiModelProperty(hidden = true)
    private BigDecimal vatValue;

    @ApiModelProperty(value = "Product tax rate", required = true)
    private Vat vatRate;

    public InvoiceEntry(String description, BigDecimal price, Vat vatRate) {
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
        this.vatValue = price.multiply(new BigDecimal(Float.toString(vatRate.getRate())));
    }
}
