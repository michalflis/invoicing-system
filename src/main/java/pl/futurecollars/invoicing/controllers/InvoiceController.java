package pl.futurecollars.invoicing.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/invoices", produces = {"application/json;charset=UTF-8"})

public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> save(@RequestBody Invoice invoice) {
        return ResponseEntity.ok()
            .body(invoiceService.save(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAll() {
        return ResponseEntity.ok()
            .body(new ArrayList<>(invoiceService.getAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok()
                .body(invoiceService.getById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<Invoice> update(@RequestBody Invoice invoice) {
        return ResponseEntity.ok()
            .body(invoiceService.update(invoice));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> update(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok()
                .body(invoiceService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
