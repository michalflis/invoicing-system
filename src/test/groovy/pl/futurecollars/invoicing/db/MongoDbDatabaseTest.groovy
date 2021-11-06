package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue

@SpringBootTest
@IfProfileValue(name = "invoicing-system.database", value = "mongo")
class MongoDbDatabaseTest extends DatabaseTest {

    @Autowired
    private MongoDbDatabase mongoDbDatabase

    Database getDatabaseInstance() {
        assert mongoDbDatabase != null
        mongoDbDatabase
    }
}


