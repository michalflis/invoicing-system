package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@Stepwise
@AutoConfigureMockMvc
class InvoiceControllerBasicTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService<Invoice> jsonService

    @Autowired
    private JsonService<Invoice[]> jsonListService

    @Shared
    def invoice = InvoiceFixture.invoice(1)

    @Shared
    def updatedInvoice = InvoiceFixture.invoice(1)

    @Shared
    UUID id

    def "should add single invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def response = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        id = jsonService.convertToObject(response, Invoice.class).getId()
        invoice.setId(id)

        then:
        invoice == jsonService.convertToObject(response, Invoice.class)
    }

    def "should return list of invoices"() {
        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoices.size() > 0
        invoices[0] == invoice
    }

    def "should update invoice"() {
        given:
        updatedInvoice.setId(id)
        def updatedInvoiceAsJson = jsonService.convertToJson(updatedInvoice)

        when:
        def response = mockMvc.perform(
                put("/invoices").content(updatedInvoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedInvoice == jsonService.convertToObject(response, Invoice.class)
    }

    def "should return updatedInvoice by id"() {
        when:
        def response = mockMvc.perform(get("/invoices/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedInvoice == jsonService.convertToObject(response, Invoice.class)
    }

    def "should delete invoice by id"() {
        when:
        def response = mockMvc.perform(delete("/invoices/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "true"
    }

    def "should return empty list of invoices"() {
        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoices.size() == 0
        response == "[]"
    }
}


