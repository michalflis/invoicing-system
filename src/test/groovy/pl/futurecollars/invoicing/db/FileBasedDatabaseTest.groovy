package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService

class FileBasedDatabaseTest extends DatabaseTest {
    @Override
    Database getDatabaseInstance() {
        def fileService = new FileService()
        fileService.clearDatabase()
        def invoiceService = new JsonService<Invoice>()
        def idService = new JsonService<UUID>()
        return new FileBasedDatabase(fileService, invoiceService, idService)
    }
}
