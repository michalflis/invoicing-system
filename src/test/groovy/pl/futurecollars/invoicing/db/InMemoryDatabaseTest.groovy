package pl.futurecollars.invoicing.db

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties="invoicing-system.database=memory")
class InMemoryDatabaseTest extends DatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
    }
}
