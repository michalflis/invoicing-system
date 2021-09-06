package pl.futurecollars.invoicing.db

import spock.lang.Specification

class InMemoryDatabaseTest extends DatabaseTest {


    @Override
    Database getDatabaseInstance() {

        return new InMemoryDatabase()

    }
}
