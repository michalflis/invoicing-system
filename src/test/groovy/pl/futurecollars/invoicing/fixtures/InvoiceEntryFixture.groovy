package pl.futurecollars.invoicing.fixtures

import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

class InvoiceEntryFixture {

    static product(int id) {
        new InvoiceEntry("Product $id"
                , BigDecimal.valueOf(100 * id)
                , Vat.VAT_23
        )
    }
}
