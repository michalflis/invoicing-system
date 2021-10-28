package pl.futurecollars.invoicing.db

import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

import javax.sql.DataSource

@SpringBootTest(properties = "invoicing-system.database=sql")
class SqlDatabaseTest extends DatabaseTest {

    @Override
    Database getDatabaseInstance() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        def database = new SqlDatabase(jdbcTemplate)

        return database
    }
}

