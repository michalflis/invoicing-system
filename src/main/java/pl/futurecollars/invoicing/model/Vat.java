package pl.futurecollars.invoicing.model;

public enum Vat {

    VAT_23(0.23F),
    VAT_8(0.08F),
    VAT_5(0.05F),
    VAT_0(0F),
    VAT_ZW(0F);

    private final float rate;

    Vat(float rate) {

        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }
}
