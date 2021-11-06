package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = "invoicing-system.database=mongo")
class MongoDbDatabaseTest extends DatabaseTest {

    @Autowired
    private MongoDbDatabase mongoDbDatabase

    Database getDatabaseInstance() {
        assert mongoDbDatabase != null
        mongoDbDatabase
    }
}


