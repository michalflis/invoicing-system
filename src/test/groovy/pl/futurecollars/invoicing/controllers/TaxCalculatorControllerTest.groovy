package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.TaxReport
import pl.futurecollars.invoicing.service.TaxCalculatorService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService<Invoice> jsonServiceInvoice

    @Autowired
    private JsonService<TaxReport> jsonServiceTaxReport

    @Autowired
    private Database database

    @Shared
    def invoice = InvoiceFixture.invoice(1)
    def invoice1 = InvoiceFixture.invoice(2)

    def setup() {
        database.clear()

        def invoiceAsJson = jsonServiceInvoice.convertToJson(invoice)
        def invoice1AsJson = jsonServiceInvoice.convertToJson(invoice1)

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

    def "Should get tax report for company(2)"() {
        given:
        def taxIdentificationNumber = invoice1.getIssuer().getTaxIdentificationNumber()
        def taxReport = new TaxReport().setIncomingVat(BigDecimal.valueOf(414))
                .setOutgoingVat(BigDecimal.valueOf(138))
                .setIncome(BigDecimal.valueOf(1800))
                .setCosts(BigDecimal.valueOf(600))
                .setEarnings(BigDecimal.valueOf(1200))
                .setVatToPay(BigDecimal.valueOf(276))
                .build()

        when:
        def response = mockMvc.perform(get("/tax/" + taxIdentificationNumber))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonServiceTaxReport.convertToObject(response, TaxReport.class) == taxReport
    }
}
