package pl.futurecollars.invoicing.config;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.MongoDbDatabase;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
public class MongoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public MongoDatabase mongoDb() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();

        MongoClient client = MongoClients.create(settings);
        return client.getDatabase("invoices");
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public Database mongoDatabase(MongoDatabase mongoDb) {

        MongoCollection<Invoice> mongoCollection = mongoDb.getCollection("invoice", Invoice.class);
        return new MongoDbDatabase(mongoCollection);
    }
}
