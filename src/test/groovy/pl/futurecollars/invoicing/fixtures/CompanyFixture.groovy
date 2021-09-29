package pl.futurecollars.invoicing.fixtures

import pl.futurecollars.invoicing.model.Company

class CompanyFixture {

    static company(int id) {

        new Company("123-45-6$id-819"
                , "Ul. Kubusia Puchatka 13/$id, 01-001 Pułtusk"
                , "Company $id")

    }
}