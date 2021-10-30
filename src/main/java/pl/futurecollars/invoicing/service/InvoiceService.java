package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.dto.InvoiceListDto;
import pl.futurecollars.invoicing.dto.mappers.InvoiceListMapper;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final Database database;
    private final InvoiceListMapper invoiceListMapper;

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
            .filter((Invoice invoice) -> issuerName.equals(invoice.getIssuer().getName()))
            .collect(Collectors.toList());
    }

    public List<Invoice> filterByReceiverName(String receiverName) {
        return database.getAll()
            .stream()
            .filter((Invoice invoice) -> receiverName.equals(invoice.getReceiver().getName()))
            .collect(Collectors.toList());
    }

    public List<Invoice> filterByDate(LocalDate date) {
        return database.getAll()
            .stream()
            .filter((Invoice invoice) -> date.equals(invoice.getDate()))
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

    public List<InvoiceListDto> getList() {
        return database.getAll().stream().map(invoiceListMapper::toDto).collect(Collectors.toList());
    }

    public Invoice update(Invoice updatedInvoice) {
        return database.update(updatedInvoice);
    }

    public boolean delete(UUID id) {
        return database.delete(id);
    }
}
