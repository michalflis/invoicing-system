package pl.futurecollars.invoicing.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.dto.InvoiceListDto;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InvoiceController implements InvoiceControllerApi {

    private final InvoiceService invoiceService;

    @Override
    public ResponseEntity<Invoice> save(@RequestBody Invoice invoice) {
        log.debug("Adding new invoice to database");
        return ResponseEntity.ok()
            .body(invoiceService.save(invoice));
    }

    @Override
    public ResponseEntity<List<Invoice>> getAll() {
        log.debug("Getting all invoices from database");
        return ResponseEntity.ok()
            .body(new ArrayList<>(invoiceService.getAll()));
    }

    @Override
    public ResponseEntity<List<InvoiceListDto>> getList() {
        log.debug("Getting list of invoices from database");
        return ResponseEntity.ok()
            .body(new ArrayList<>(invoiceService.getList()));
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable UUID id) {
        log.debug("Getting invoice ID: {} from database", id);
        try {
            return ResponseEntity.ok()
                .body(invoiceService.getById(id).get());
        } catch (Exception e) {
            log.error("Exception: {} occurred while getting invoice ID: {} from database", e, id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Invoice> update(@RequestBody Invoice invoice) {
        log.debug("Updating invoice ID: {} in database", invoice.getId());
        return ResponseEntity.ok()
            .body(invoiceService.update(invoice));
    }

    @Override
    public ResponseEntity<Boolean> update(@PathVariable UUID id) {
        log.debug("Deleting invoice ID: {} from database", id);
        try {
            return ResponseEntity.ok()
                .body(invoiceService.delete(id));
        } catch (Exception e) {
            log.error("Exception: {} occurred while deleting invoice ID: {} from database", e, id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
