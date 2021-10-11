package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties="invoicing-system.database=file")
class FileBasedDatabaseTest extends DatabaseTest {

    @Autowired
    FileBasedDatabase fileBasedDatabase

    @Override
    Database getDatabaseInstance() {
        return fileBasedDatabase
    }
}
