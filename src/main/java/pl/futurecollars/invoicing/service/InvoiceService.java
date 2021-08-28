package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import pl.futurecollars.invoicing.db.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Data
public class InvoiceService {

    private final InMemoryDatabase database;

    public BigDecimal getTotalNet(Invoice invoice) {
        return invoice.getEntries()
            .stream()
            .map(InvoiceEntry::getPrice)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

    public BigDecimal getTotalTaxValue(Invoice invoice) {
        return invoice.getEntries()
            .stream()
            .map(InvoiceEntry::getVatValue)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

    public BigDecimal getTotalGross(Invoice invoice) {
        return getTotalNet(invoice).add(getTotalTaxValue(invoice));
    }

    public List<Invoice> filterByIssuerName(String issuerName) {
        return database.getAll()
            .stream()
            .filter((Invoice invoice) -> invoice.getIssuer().getName().equals(issuerName))
            .collect(Collectors.toList());
    }

    public List<Invoice> filterByReceiverName(String receiverName) {
        return database.getAll()
            .stream()
            .filter((Invoice invoice) -> invoice.getReceiver().getName().equals(receiverName))
            .collect(Collectors.toList());
    }

    public List<Invoice> filterByDate(Date date) {
        return database.getAll()
            .stream()
            .filter((Invoice invoice) -> invoice.getDate().equals(date))
            .collect(Collectors.toList());
    }

    public Invoice save(Invoice invoice) {
        return database.save(invoice);
    }

    public Optional<Invoice> getById(UUID id) {
        return Optional.ofNullable(database.getById(id));
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public Invoice update(Invoice updatedInvoice) {
        return database.update(updatedInvoice);
    }

    public boolean delete(UUID id) {
        return database.delete(id);
    }
}
