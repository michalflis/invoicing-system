package pl.futurecollars.invoicing.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RequiredArgsConstructor
@RestController
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> save(@RequestBody Invoice invoice) {
        return ResponseEntity.ok()
            .body(invoiceService.save(invoice));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<UUID>> getAll() {
        return ResponseEntity.ok()
            .body(invoiceService.getAll()
                .stream()
                .map(Invoice::getId)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Optional<Invoice>> getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok()
                .body(invoiceService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<Invoice> update(@RequestBody Invoice invoice) {
        return ResponseEntity.ok()
            .body(invoiceService.update(invoice));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> update(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok()
                .body(invoiceService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
