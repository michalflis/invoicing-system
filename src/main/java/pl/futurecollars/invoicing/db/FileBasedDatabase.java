package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

public class FileBasedDatabase implements Database {

    FileService fileService = new FileService();
    JsonService<Invoice> invoiceService = new JsonService<>();
    JsonService<UUID> idService = new JsonService<>();

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice.getId() == null) {
            UUID id = UUID.randomUUID();
            invoice.setId(id);

            if (fileService.containsID(id)) {
                return save(invoice);
            }
        }
        fileService.writeToDatabase(invoiceService.convertToJson(invoice));
        fileService.writeToIdKeeper(idService.convertToJson(invoice.getId()));
        return invoice;
    }

    @Override
    public Invoice getById(UUID id) {
        if (fileService.containsID(id)) {
            return invoiceService.covertToObject(fileService.readFromDatabase()
                    .stream()
                    .filter(string -> string.contains(id.toString()))
                    .collect(Collectors.toList())
                    .get(0),
                Invoice.class);
        }
        return null;
    }

    @Override
    public List<Invoice> getAll() {
        return fileService.readFromDatabase()
            .stream()
            .map(string -> invoiceService.covertToObject(string, Invoice.class))
            .collect(Collectors.toList());
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        delete(updatedInvoice.getId());
        save(updatedInvoice);
        return updatedInvoice;
    }

    @Override
    public boolean delete(UUID id) {
        if (fileService.containsID(id)) {
            List<Invoice> databaseCopy = getAll();
            fileService.clearDatabase();
            databaseCopy
                .stream()
                .filter(invoice -> !invoice.getId().equals(id))
                .forEach(this::save);
            return true;
        }
        return false;
    }

}
