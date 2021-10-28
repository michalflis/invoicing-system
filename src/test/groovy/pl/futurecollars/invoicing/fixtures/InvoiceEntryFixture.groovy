package pl.futurecollars.invoicing.fixtures

import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.math.RoundingMode

class InvoiceEntryFixture {

    static product(int id) {
        new InvoiceEntry("Product $id",
                BigDecimal.valueOf(100 * id).setScale(2, RoundingMode.HALF_UP),
                Vat.VAT_23
        )
    }

    static gasolineForTheCarUsedForPersonalReason() {
        new InvoiceEntry("Gasoline",
                BigDecimal.valueOf(101).setScale(2, RoundingMode.HALF_UP),
                Vat.VAT_23,
                "H1 00001",
                true
        )
    }
}
