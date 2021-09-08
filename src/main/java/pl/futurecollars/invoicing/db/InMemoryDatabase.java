package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import pl.futurecollars.invoicing.model.Invoice;

@Component
public class InMemoryDatabase implements Database {

    private Map<UUID, Invoice> database = new HashMap<>();

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
        return database.put(updatedInvoice.getId(), updatedInvoice);
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
}
