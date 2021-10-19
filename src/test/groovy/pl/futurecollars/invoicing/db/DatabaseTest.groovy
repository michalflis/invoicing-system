package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import spock.lang.Specification
import java.time.LocalDate

abstract class DatabaseTest extends Specification {

    abstract Database getDatabaseInstance();

    def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX", 1000.00, 1000.00)
    def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY", 1000.00, 1000.00)
    def date = new LocalDate(2021, 5, 5)
    def entries = new ArrayList<InvoiceEntry>();
    def invoice = new Invoice(date, issuer, receiver, entries)
    Database database

    def setup() {
        database = getDatabaseInstance()
        database.clear()
    }

    def "should save invoice in to database"() {
        when:
        def result = database.save(invoice)

        then:
        database.getById(result.getId()) != null
        database.getById(result.getId()).getIssuer().getName() == "XXX"

    }

    def "should get invoice from database by Id"() {
        setup:
        database.save(invoice)

        when:
        def result = database.getById(invoice.getId())

        then:
        result != null
        result.getIssuer().getName() == "XXX"
    }

    def "should get list of all invoices from database"() {
        setup:
        def invoice2 = new Invoice(date, issuer, receiver, entries)
        def invoice3 = new Invoice(date, issuer, receiver, entries)
        database.save(invoice)
        database.save(invoice2)
        database.save(invoice3)

        when:
        def result = database.getAll()

        then:
        result.size() == 3
    }

    def "should update invoice in the database"() {
        setup:
        def issuerUpdated = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "CCC", 1000.00, 1000.00)
        def invoiceUpdated = new Invoice(date, issuerUpdated, receiver, entries)
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
        database.save(invoice)

        when:
        def result = database.delete(invoice.getId())

        then:
        result
        database.getAll().size() == 0
    }

    def "should delete not existing invoice from database"() {
        when:
        def result = database.delete(UUID.randomUUID())

        then:
        !result
        database.getAll().size() == 0
    }

    def "should remove all invoices form database"() {
        setup:
        def invoice2 = new Invoice(date, issuer, receiver, entries)
        def invoice3 = new Invoice(date, issuer, receiver, entries)
        database.save(invoice)
        database.save(invoice2)
        database.save(invoice3)

        when:
        def result = database.clear()

        then:
        database.getAll().size() == 0
    }
}
