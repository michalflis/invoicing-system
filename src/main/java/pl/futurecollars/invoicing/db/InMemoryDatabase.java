package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Invoice;

@Data
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
@Service
public class InMemoryDatabase implements Database {

    private final HashMap<UUID, Invoice> database = new HashMap<>();

    @Override
    public Invoice save(Invoice invoice) {
        UUID id = UUID.randomUUID();
        invoice.setId(id);

        if (database.get(id) != null) {
            return save(invoice);
        }

        database.put(id, invoice);
        return invoice;
    }

    @Override
    public Invoice getById(UUID id) {
        return database.get(id);
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        if (database.containsKey(updatedInvoice.getId())) {
            database.put(updatedInvoice.getId(), updatedInvoice);
            return updatedInvoice;
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        try {
            if (!database.containsKey(id)) {
                return false;
            }
            database.remove(id);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        database.clear();
    }
}
