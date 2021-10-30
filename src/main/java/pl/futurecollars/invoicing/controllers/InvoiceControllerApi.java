package pl.futurecollars.invoicing.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.dto.InvoiceListDto;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping(path = "/invoices", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"invoice-controller"})
public interface InvoiceControllerApi {

    @ApiOperation("Add new invoice")
    @PostMapping
    ResponseEntity<Invoice> save(@RequestBody Invoice invoice);

    @ApiOperation("Get list of all invoices")
    @GetMapping
    ResponseEntity<List<Invoice>> getAll();

    @ApiOperation("Get short list of all invoices")
    @GetMapping(path = "/list")
    ResponseEntity<List<InvoiceListDto>> getList();

    @ApiOperation("Get invoice by ID")
    @GetMapping(path = "/{id}")
    ResponseEntity<Invoice> getById(@PathVariable UUID id);

    @ApiOperation("Update given invoice")
    @PutMapping
    ResponseEntity<Invoice> update(@RequestBody Invoice invoice);

    @ApiOperation("Delete invoice by ID")
    @DeleteMapping(path = "/{id}")
    ResponseEntity<Boolean> update(@PathVariable UUID id);
}
