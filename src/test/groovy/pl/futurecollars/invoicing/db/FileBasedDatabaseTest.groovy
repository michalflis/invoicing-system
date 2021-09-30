package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.SpringBootTest
import pl.futurecollars.invoicing.utils.FileService

@ConditionalOnProperty(name="invoicing-system.database", havingValue = "file")
@SpringBootTest
class FileBasedDatabaseTest extends DatabaseTest {

    @Autowired
    FileBasedDatabase fileBasedDatabase

    @Override
    Database getDatabaseInstance() {
        def fileService = new FileService()
        fileService.clearDatabase()
        return fileBasedDatabase
    }
}
