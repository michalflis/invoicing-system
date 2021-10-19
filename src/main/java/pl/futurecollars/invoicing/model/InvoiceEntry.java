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

    @ApiModelProperty(value = "Mark if car is also used for personal reasons")
    private Boolean carUsedForPersonalReason;

    @ApiModelProperty(value = "Fill car registration number if cost is related to company car",
        example = "H1 00001")
    private String carRegistrationNumber;

    public InvoiceEntry(String description, BigDecimal price, Vat vatRate) {
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
        this.vatValue = price.multiply(new BigDecimal(Float.toString(vatRate.getRate())));
        this.carRegistrationNumber = "";
        this.carUsedForPersonalReason = false;
    }

    public InvoiceEntry(String description, BigDecimal price, Vat vatRate, String carRegistrationNumber, Boolean carUsedForPersonalReason) {
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
        this.vatValue = price.multiply(new BigDecimal(Float.toString(vatRate.getRate())));
        this.carRegistrationNumber = carRegistrationNumber;
        this.carUsedForPersonalReason = carUsedForPersonalReason;
    }
}
