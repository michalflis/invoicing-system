package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.repository.InvoiceRepository

@DataJpaTest
@IfProfileValue(name = "invoicing-system.database", value = "jpa")
class JpaDatabaseTest extends DatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    Database getDatabaseInstance() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }
}


