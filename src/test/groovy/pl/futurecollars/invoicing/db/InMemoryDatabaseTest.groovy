package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.service.InvoiceService
import spock.lang.Specification

class InMemoryDatabaseTest extends Specification {
    def "should save invoice in to database"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()

        when:
        def result = database.save(invoice)

        then:
        database.getById(result.getId()) != null
        database.getById(result.getId()).getIssuer().getName() == "XXX"
    }

    def "should get invoice from database by Id"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)

        when:
        def result = database.getById(invoice.getId())

        then:
        result != null
        result.getIssuer().getName() == "XXX"
    }

    def "should get list of all invoices from database"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice1 = new Invoice(date, issuer, receiver, entries)
        def invoice2 = new Invoice(date, issuer, receiver, entries)
        def invoice3 = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice1)
        database.save(invoice2)
        database.save(invoice3)

        when:
        def result = database.getAll()

        then:
        result.size() == 3
    }

    def "should update invoice in the database"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def issuerUpdated = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "CCC")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice = new Invoice(date, issuer, receiver, entries)
        def invoiceUpdated = new Invoice(date, issuerUpdated, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)
        invoiceUpdated.setId(invoice.getId())

        when:
        def result = database.update(invoiceUpdated)

        then:
        database.getById(result.getId()) != null
        database.getById(result.getId()).getIssuer().getName() == "CCC"
    }

    def "should delete invoice from database"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)

        when:
        def result = database.delete(invoice.getId())

        then:
        result
        database.getAll().size() == 0
    }

    def "should delete unexisting invoice from database"() {
        setup:
        def database = new InMemoryDatabase()

        when:
        def result = database.delete(UUID.randomUUID())

        then:
        result
        database.getAll().size() == 0
    }

}
