package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.utils.FileService
import spock.lang.Specification
import java.time.LocalDate

class DatabaseTest extends Specification {

    def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
    def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
    def date = new LocalDate(2021, 5, 5)
    def entries = new ArrayList<InvoiceEntry>();
    def invoice = new Invoice(date, issuer, receiver, entries)
    def inMemoryDataBase = new InMemoryDatabase()
    def fileBasedDatabase = new FileBasedDatabase()

    def setup() {
        def fileService = new FileService()
        fileService.clearDatabase()
    }

    def "should save invoice in to database"() {
        when:
        def resultInMemory = inMemoryDataBase.save(invoice)
        def resultInFile = fileBasedDatabase.save(invoice)

        then:
        inMemoryDataBase.getById(resultInMemory.getId()) != null
        inMemoryDataBase.getById(resultInMemory.getId()).getIssuer().getName() == "XXX"
        fileBasedDatabase.getById(resultInFile.getId()) != null
        fileBasedDatabase.getById(resultInFile.getId()).getIssuer().getName() == "XXX"
    }

    def "should get invoice from database by Id"() {
        setup:
        inMemoryDataBase.save(invoice)
        fileBasedDatabase.save(invoice)

        when:
        def resultInMemory = inMemoryDataBase.getById(invoice.getId())
        def resultInFile = fileBasedDatabase.getById(invoice.getId())

        then:
        resultInMemory != null
        resultInMemory.getIssuer().getName() == "XXX"
        resultInFile != null
        resultInFile.getIssuer().getName() == "XXX"
    }

    def "should get list of all invoices from database"() {
        setup:
        def invoice2 = new Invoice(date, issuer, receiver, entries)
        def invoice3 = new Invoice(date, issuer, receiver, entries)
        inMemoryDataBase.save(invoice)
        inMemoryDataBase.save(invoice2)
        inMemoryDataBase.save(invoice3)
        fileBasedDatabase.save(invoice)
        fileBasedDatabase.save(invoice2)
        fileBasedDatabase.save(invoice3)

        when:
        def resultInMemory = inMemoryDataBase.getAll()
        def resultInFile = fileBasedDatabase.getAll()

        then:
        resultInMemory.size() == 3
        resultInFile.size() == 3
    }

    def "should update invoice in the database"() {
        setup:
        def issuerUpdated = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "CCC")
        def invoiceUpdated = new Invoice(date, issuerUpdated, receiver, entries)
        inMemoryDataBase.save(invoice)
        fileBasedDatabase.save(invoice)
        invoiceUpdated.setId(invoice.getId())

        when:
        def resultInMemory = inMemoryDataBase.update(invoiceUpdated)
        def resultInFile = fileBasedDatabase.update(invoiceUpdated)
        resultInFile
        then:
        inMemoryDataBase.getById(resultInMemory.getId()) != null
        inMemoryDataBase.getById(resultInMemory.getId()).getIssuer().getName() == "CCC"
        fileBasedDatabase.getById(resultInFile.getId()) != null
        fileBasedDatabase.getById(resultInFile.getId()).getIssuer().getName() == "CCC"

    }

    def "should delete invoice from database"() {
        setup:
        inMemoryDataBase.save(invoice)
        fileBasedDatabase.save(invoice)

        when:
        def resultInMemory = inMemoryDataBase.delete(invoice.getId())
        def resultInFile = fileBasedDatabase.delete(invoice.getId())

        then:
        resultInMemory
        resultInFile
        inMemoryDataBase.getAll().size() == 0
        fileBasedDatabase.getAll().size() == 0
    }

    def "should delete not existing invoice from database"() {
        when:
        def resultInMemory = inMemoryDataBase.delete(UUID.randomUUID())
        def resultInFile = fileBasedDatabase.delete(UUID.randomUUID())

        then:
        !resultInMemory
        !resultInFile
        inMemoryDataBase.getAll().size() == 0
        fileBasedDatabase.getAll().size() == 0
    }
}
