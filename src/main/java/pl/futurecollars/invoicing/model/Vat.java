package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;

public enum Vat {

    VAT_23(0.23),
    VAT_8(0.08),
    VAT_5(0.05),
    VAT_0(0),
    VAT_ZW(0);

    private final BigDecimal rate;

    Vat(double rate) {

        this.rate = BigDecimal.valueOf(rate);
    }

    public BigDecimal getRate() {
        return rate;
    }
}
