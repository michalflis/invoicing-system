package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.repository.InvoiceRepository;

@AllArgsConstructor
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
@Service
public class JpaDatabase implements Database {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        invoice.setId(UUID.randomUUID());
        if (invoiceRepository.findById(invoice.getId()).isPresent()) {
            return save(invoice);
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(UUID id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice id: " + id + " does not exist"));
    }

    @Override
    public List<Invoice> getAll() {
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceRepository.findAll().forEach(invoiceList::add);
        return invoiceList;
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        if (invoiceRepository.findById(updatedInvoice.getId()).isPresent()) {
            return invoiceRepository.save(updatedInvoice);
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        if (invoiceRepository.findById(id).isPresent()) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        invoiceRepository.deleteAll();
    }
}
