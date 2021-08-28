package pl.futurecollars.invoicing.model

import spock.lang.Specification

class InvoiceEntryTest extends Specification {

    def "should build InvoiceEntry object"() {
        setup:
        def invoiceEntry = new InvoiceEntry("Gwoździe", 20 as BigDecimal, Vat.VAT_5)

        when:
        def result = invoiceEntry

        then:
        result != null
    }

    def "should calculate VAT value"() {
        setup:
        def invoiceEntry = new InvoiceEntry("Gwoździe", 20 as BigDecimal, Vat.VAT_5)

        when:
        def result = invoiceEntry.vatValue

        then:
        result == 1
    }


}
