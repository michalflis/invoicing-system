package pl.futurecollars.invoicing.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerExhaustiveTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private FileService fileService

    def cleanup() { fileService.clearDatabase() }

    @Autowired
    private JsonService<Invoice> jsonService

    @Autowired
    private JsonService<Invoice[]> jsonListService

    @Shared
    def invoice = InvoiceFixture.invoice(1)
    def invoice1 = InvoiceFixture.invoice(2)
    def invoice2 = InvoiceFixture.invoice(3)

    def "should add 3 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)
        def invoice2AsJson = jsonService.convertToJson(invoice2)

        expect:
        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice2AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    def "should return list of 2 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)

        when:
        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        invoice.setId(invoices[0].getId())
        invoice1.setId(invoices[1].getId())

        then:
        invoices.length == 2
        invoices[0] == invoice
        invoices[1] == invoice1
    }

    def "should update not existing invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def id = jsonService.convertToObject(postResponse, Invoice.class).getId()
        invoice.setId(id)

        UUID updatedId
        do updatedId = UUID.randomUUID()
        while (updatedId == id)

        def updatedInvoice = InvoiceFixture.invoice(1)
        updatedInvoice.setId(updatedId)
        def updatedInvoiceAsJson = jsonService.convertToJson(updatedInvoice)

        def putResponse = mockMvc.perform(
                put("/invoices").content(updatedInvoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoice != updatedInvoice
        invoices[0] == invoice
        putResponse == ""
    }

    def "should delete not existing invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def id = jsonService.convertToObject(postResponse, Invoice.class).getId()
        invoice.setId(id)

        UUID updatedId
        do updatedId = UUID.randomUUID()
        while (updatedId == id)

        def deleteResponse = mockMvc.perform(
                delete("/invoices/" + updatedId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        deleteResponse == "false"
        invoices[0] == invoice
    }

    def "should delete second of 3 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)
        def invoice2AsJson = jsonService.convertToJson(invoice2)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def post1Response = mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def post2Response = mockMvc.perform(
                post("/invoices").content(invoice2AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def id = jsonService.convertToObject(postResponse, Invoice.class).getId()
        invoice.setId(id)
        def id1 = jsonService.convertToObject(post1Response, Invoice.class).getId()
        invoice1.setId(id1)
        def id2 = jsonService.convertToObject(post2Response, Invoice.class).getId()
        invoice2.setId(id2)

        def deleteResponse = mockMvc.perform(
                delete("/invoices/" + id1))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        deleteResponse == "true"
        invoices.length == 2
        invoices[0] == invoice
        invoices[1] == invoice2
    }
}


