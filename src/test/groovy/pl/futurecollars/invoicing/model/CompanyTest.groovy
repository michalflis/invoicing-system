package pl.futurecollars.invoicing.model

import spock.lang.Specification

class CompanyTest extends Specification {

    def "should build Company object"() {
        setup:
        def company = new Company("123-45-67-819", "Ul. Kubusia Puchatka 13/2, 01-001 Pułtusk", "XXX")

        when:
        def result = company

        then:
        result != null
    }

    def "should do nothing"() {

    }

}
