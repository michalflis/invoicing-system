package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.TaxReport

import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

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
    private JsonService<Company> jsonServiceCompany

    @Autowired
    private JsonService<TaxReport> jsonServiceTaxReport

    @Autowired
    private Database database

    @Shared
    def invoice = InvoiceFixture.invoice(0)
    def invoice1 = InvoiceFixture.invoice(4)

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
    }

    def cleanup() { database.clear() }

    def "Should get tax report for company(4)"() {
        given:
        def companyAsJson = jsonServiceCompany.convertToJson(invoice1.getIssuer())
        def taxReport = new TaxReport.TaxReportBuilder()
                .setIncomingVat(BigDecimal.valueOf(345))
                .setOutgoingVat(BigDecimal.valueOf(0))
                .setIncome(BigDecimal.valueOf(1500))
                .setCosts(BigDecimal.valueOf(0))
                .setIncomeMinusCosts(BigDecimal.valueOf(1500))
                .setVatToPay(BigDecimal.valueOf(345))
                .setPensionInsurance(BigDecimal.valueOf(500.97))
                .setIncomeMinusCostsMinusPensionInsurance(BigDecimal.valueOf(999.03))
                .setTaxCalculationBase(BigDecimal.valueOf(999))
                .setIncomeTax(BigDecimal.valueOf(189.81))
                .setHealthInsurance9(BigDecimal.valueOf(90))
                .setHealthInsurance775(BigDecimal.valueOf(80))
                .setIncomeTaxMinusHealthInsurance(BigDecimal.valueOf(109.81))
                .setFinalIncomeTaxValue(BigDecimal.valueOf(109))
                .build()

        when:
        def response = mockMvc.perform(
                post("/tax").content(companyAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonServiceTaxReport.convertToObject(response, TaxReport.class) == taxReport
    }
}
