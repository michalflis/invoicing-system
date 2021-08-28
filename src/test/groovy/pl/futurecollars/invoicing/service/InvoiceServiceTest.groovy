package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.InMemoryDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

class InvoiceServiceTest extends Specification {
    def "should calculate total net value of all invoice entries"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def invoiceEntry1 = new InvoiceEntry("Gwoździe", 20 as BigDecimal, Vat.VAT_5)
        def invoiceEntry2 = new InvoiceEntry("Młotek", 35 as BigDecimal, Vat.VAT_23)
        def entries = Arrays.asList(invoiceEntry1, invoiceEntry2);
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.getTotalNet(invoice)

        then:
        result == 55
    }

    def "should calculate total tax value of all invoice entries"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def invoiceEntry1 = new InvoiceEntry("Gwoździe", 20 as BigDecimal, Vat.VAT_5)
        def invoiceEntry2 = new InvoiceEntry("Młotek", 35 as BigDecimal, Vat.VAT_23)
        def entries = Arrays.asList(invoiceEntry1, invoiceEntry2);
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.getTotalTaxValue(invoice)

        then:
        result == 9.05
    }

    def "should calculate total gross value of all invoice entries"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def invoiceEntry1 = new InvoiceEntry("Gwoździe", 20 as BigDecimal, Vat.VAT_5)
        def invoiceEntry2 = new InvoiceEntry("Młotek", 35 as BigDecimal, Vat.VAT_23)
        def entries = Arrays.asList(invoiceEntry1, invoiceEntry2);
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice)
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.getTotalGross(invoice)

        then:
        result == 64.05
    }

    def "should filter list of invoices by issuer name"() {
        setup:
        def issuer1 = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def issuer2 = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "AAA")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice1 = new Invoice(date, issuer1, receiver, entries)
        def invoice2 = new Invoice(date, issuer1, receiver, entries)
        def invoice3 = new Invoice(date, issuer2, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice1)
        database.save(invoice2)
        database.save(invoice3)
        def invoiceService = new InvoiceService(database)

        when:
        def resultIssuer1 = invoiceService.filterByIssuerName("XXX")
        def resultIssuer2 = invoiceService.filterByIssuerName("AAA")

        then:
        resultIssuer1.size() == 2
        resultIssuer2.size() == 1
    }

    def "should filter list of invoices by receiver name"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver1 = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "AAA")
        def receiver2 = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice1 = new Invoice(date, issuer, receiver1, entries)
        def invoice2 = new Invoice(date, issuer, receiver2, entries)
        def invoice3 = new Invoice(date, issuer, receiver2, entries)
        def database = new InMemoryDatabase()
        database.save(invoice1)
        database.save(invoice2)
        database.save(invoice3)
        def invoiceService = new InvoiceService(database)

        when:
        def resultReceiver1 = invoiceService.filterByReceiverName("AAA")
        def resultReceiver2 = invoiceService.filterByReceiverName("YYY")

        then:
        resultReceiver1.size() == 1
        resultReceiver2.size() == 2
    }

    def "should filter list of invoices by issue date"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date1 = new Date(2021 - 05 - 05)
        def date2 = new Date(2020 - 01 - 17)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice1 = new Invoice(date1, issuer, receiver, entries)
        def invoice2 = new Invoice(date1, issuer, receiver, entries)
        def invoice3 = new Invoice(date2, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        database.save(invoice1)
        database.save(invoice2)
        database.save(invoice3)
        def invoiceService = new InvoiceService(database)

        when:
        def resultDate1 = invoiceService.filterByDate(date1)
        def resultDate2 = invoiceService.filterByDate(date2)

        then:
        resultDate1.size() == 2
        resultDate2.size() == 1
    }

    def "should save invoice in to database"() {
        setup:
        def issuer = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")
        def receiver = new Company("123-22-98-748", "Ul. Kaczki Balbinki 17c/23, 02-358 Straszyn", "YYY")
        def date = new Date(2021 - 05 - 05)
        def entries = new ArrayList<InvoiceEntry>();
        def invoice = new Invoice(date, issuer, receiver, entries)
        def database = new InMemoryDatabase()
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.save(invoice)

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
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.getById(invoice.getId())

        then:
        result != null
        result.get().getIssuer().getName() == "XXX"
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
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.getAll()

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
        def invoiceService = new InvoiceService(database)
        invoiceUpdated.setId(invoice.getId())

        when:
        def result = invoiceService.update(invoiceUpdated)

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
        def invoiceService = new InvoiceService(database)

        when:
        def result = invoiceService.delete(invoice.getId())

        then:
        result
        invoiceService.getAll().size() == 0
    }
}
