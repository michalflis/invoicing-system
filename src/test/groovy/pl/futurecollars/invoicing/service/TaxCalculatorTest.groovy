package pl.futurecollars.invoicing.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private TaxCalculatorService taxCalculatorService

    @Autowired
    private JsonService<Invoice> jsonService

    @Autowired
    private Database database

    @Shared
    def invoice = InvoiceFixture.invoice(1)
    def invoice1 = InvoiceFixture.invoice(2)

    def setup() {
        database.clear()

        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)

        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

    }

    def cleanup() { database.clear() }

    def "should calculate income for company(2)"() {
        when:
        def result = taxCalculatorService.income(invoice1.getIssuer().getTaxIdentificationNumber())

        then:
        result == 1800
    }

    def "should calculate cost for company(2)"() {
        when:
        def result = taxCalculatorService.costs(invoice.getReceiver().getTaxIdentificationNumber())

        then:
        result == 600
    }

    def "should calculate incoming VAT for company(2)"() {
        when:
        def result = taxCalculatorService.incomingVat(invoice1.getIssuer().getTaxIdentificationNumber())

        then:
        result == 414
    }

    def "should calculate outgoing VAT for company(2)"() {
        when:
        def result = taxCalculatorService.outgoingVat(invoice.getReceiver().getTaxIdentificationNumber())

        then:
        result == 138
    }

    def "should calculate earnings for company(2)"() {
        when:
        def result = taxCalculatorService.earnings(invoice1.getIssuer().getTaxIdentificationNumber())

        then:
        result == 1200
    }

    def "should calculate VAT to pay for company(2)"() {
        when:
        def result = taxCalculatorService.vatToPay(invoice1.getIssuer().getTaxIdentificationNumber())

        then:
        result == 276
    }

    def "should generate tax Report for company(2)"() {
        when:
        def result = taxCalculatorService.taxReport(invoice1.getIssuer().getTaxIdentificationNumber())

        then:
        result.getIncome() == 1800
        result.getCosts() == 600
        result.getIncomingVat() == 414
        result.getOutgoingVat() == 138
        result.getEarnings() == 1200
        result.getVatToPay() == 276
    }
}
