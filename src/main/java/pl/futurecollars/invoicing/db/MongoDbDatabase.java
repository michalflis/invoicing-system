package pl.futurecollars.invoicing.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
public class MongoDbDatabase implements Database {

    private final MongoCollection<Invoice> mongoCollection;

    @Override
    public Invoice save(Invoice invoice) {
        invoice.setId(UUID.randomUUID());
        if (mongoCollection.find(idFilter(invoice.getId())).first() != null) {
            return save(invoice);
        }
        mongoCollection.insertOne(invoice);
        return invoice;
    }

    @Override
    public Invoice getById(UUID id) {
        return mongoCollection.find(idFilter(id)).first();
    }

    @Override
    public List<Invoice> getAll() {
        return StreamSupport
            .stream(mongoCollection.find().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        mongoCollection.findOneAndReplace(idFilter(updatedInvoice.getId()), updatedInvoice);
        return updatedInvoice;
    }

    @Override
    public boolean delete(UUID id) {
        if (mongoCollection.find(idFilter(id)).first() == null) {
            return false;
        } else {
            mongoCollection.findOneAndDelete(idFilter(id));
            return true;
        }
    }

    @Override
    public void clear() {
        mongoCollection.deleteMany(new BasicDBObject());
    }

    private Document idFilter(UUID id) {
        return new Document("_id", id);
    }
}
