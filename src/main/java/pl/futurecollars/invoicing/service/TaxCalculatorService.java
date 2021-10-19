package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxReport;

@RequiredArgsConstructor
@Service
public class TaxCalculatorService {

    private final Database database;

    private BigDecimal basicCalculation(Predicate<Invoice> predicate, Function<InvoiceEntry, BigDecimal> function) {
        return database.getAll()
            .stream()
            .filter(predicate)
            .flatMap(invoice -> invoice.getEntries().stream())
            .map(function)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal extendedCalculation(Predicate<Invoice> firstFilter, Predicate<InvoiceEntry> secondFilter,
                                           Function<InvoiceEntry, BigDecimal> function) {
        return database.getAll()
            .stream()
            .filter(firstFilter)
            .flatMap(invoice -> invoice.getEntries().stream())
            .filter(secondFilter)
            .map(function)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal personalUsageCarVat(String taxIdentificationNumber) {
        return extendedCalculation(invoice -> invoice.getIssuer()
                .getTaxIdentificationNumber().equals(taxIdentificationNumber),
            InvoiceEntry::getCarUsedForPersonalReason,
            InvoiceEntry::getVatValue);
    }

    private BigDecimal personalUsageCarVatReduction(String taxIdentificationNumber) {
        return personalUsageCarVat(taxIdentificationNumber).divide(BigDecimal.valueOf(2), 2, RoundingMode.DOWN);
    }

    private BigDecimal personalUsageCarCostIncrease(String taxIdentificationNumber) {
        return personalUsageCarVat(taxIdentificationNumber).subtract(personalUsageCarVatReduction(taxIdentificationNumber));
    }

    private BigDecimal income(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getIssuer().getTaxIdentificationNumber().equals(taxIdentificationNumber),
            InvoiceEntry::getPrice);
    }

    private BigDecimal costs(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getReceiver().getTaxIdentificationNumber().equals(taxIdentificationNumber),
            InvoiceEntry::getPrice).add(personalUsageCarCostIncrease(taxIdentificationNumber));
    }

    private BigDecimal incomingVat(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getIssuer().getTaxIdentificationNumber().equals(taxIdentificationNumber),
            InvoiceEntry::getVatValue);
    }

    private BigDecimal outgoingVat(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getReceiver().getTaxIdentificationNumber().equals(taxIdentificationNumber),
            InvoiceEntry::getVatValue).subtract(personalUsageCarVatReduction(taxIdentificationNumber));
    }

    private BigDecimal incomeMinusCosts(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
    }

    private BigDecimal vatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    private BigDecimal incomeMinusCostsMinusPensionInsurance(Company company) {
        return incomeMinusCosts(company.getTaxIdentificationNumber())
            .subtract(company.getPensionInsurance());
    }

    private BigDecimal taxCalculationBase(Company company) {
        return incomeMinusCostsMinusPensionInsurance(company).setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeTax(Company company) {
        return taxCalculationBase(company).multiply(BigDecimal.valueOf(0.19).setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal healthyInsurance9(Company company) {
        return company.getHealthyInsurance().multiply(BigDecimal.valueOf(0.09).setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal healthyInsurance775(Company company) {
        return company.getHealthyInsurance().multiply(BigDecimal.valueOf(0.0775).setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal incomeTaxMinusHealthyInsurance(Company company) {
        return incomeTax(company).subtract(healthyInsurance775(company));
    }

    private BigDecimal finalIncomeTaxValue(Company company) {
        return incomeTaxMinusHealthyInsurance(company).setScale(0, RoundingMode.DOWN);
    }

    public TaxReport taxReport(Company company) {
        String taxIdentificationNumber = company.getTaxIdentificationNumber();
        return new TaxReport.TaxReportBuilder()
            .setIncomingVat(incomingVat(taxIdentificationNumber))
            .setOutgoingVat(outgoingVat(taxIdentificationNumber))
            .setIncome(income(taxIdentificationNumber))
            .setCosts(costs(taxIdentificationNumber))
            .setIncomeMinusCosts(incomeMinusCosts(taxIdentificationNumber))
            .setVatToPay(vatToPay(taxIdentificationNumber))
            .setPensionInsurance(company.getPensionInsurance())
            .setIncomeMinusCostsMinusPensionInsurance(incomeMinusCostsMinusPensionInsurance(company))
            .setTaxCalculationBase(taxCalculationBase(company))
            .setIncomeTax(incomeTax(company))
            .setHealthInsurance9(healthyInsurance9(company))
            .setHealthInsurance775(healthyInsurance775(company))
            .setIncomeTaxMinusHealthInsurance(incomeTaxMinusHealthyInsurance(company))
            .setFinalIncomeTaxValue(finalIncomeTaxValue(company))
            .build();
    }
}
