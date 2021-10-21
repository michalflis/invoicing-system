package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxReport {

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal incomeMinusCosts;
    private BigDecimal vatToPay;
    private BigDecimal pensionInsurance;
    private BigDecimal incomeMinusCostsMinusPensionInsurance;
    private BigDecimal taxCalculationBase;
    private BigDecimal incomeTax;
    private BigDecimal healthInsurance9;
    private BigDecimal healthInsurance775;
    private BigDecimal incomeTaxMinusHealthInsurance;
    private BigDecimal finalIncomeTaxValue;

    public TaxReport(BigDecimal incomingVat, BigDecimal outgoingVat, BigDecimal income, BigDecimal costs, BigDecimal incomeMinusCosts,
                     BigDecimal vatToPay, BigDecimal pensionInsurance, BigDecimal incomeMinusCostsMinusPensionInsurance,
                     BigDecimal taxCalculationBase, BigDecimal incomeTax, BigDecimal healthInsurance9, BigDecimal healthInsurance775,
                     BigDecimal incomeTaxMinusHealthInsurance, BigDecimal finalIncomeTaxValue) {
        this.incomingVat = incomingVat;
        this.outgoingVat = outgoingVat;
        this.income = income;
        this.costs = costs;
        this.incomeMinusCosts = incomeMinusCosts;
        this.vatToPay = vatToPay;
        this.pensionInsurance = pensionInsurance;
        this.incomeMinusCostsMinusPensionInsurance = incomeMinusCostsMinusPensionInsurance;
        this.taxCalculationBase = taxCalculationBase;
        this.incomeTax = incomeTax;
        this.healthInsurance9 = healthInsurance9;
        this.healthInsurance775 = healthInsurance775;
        this.incomeTaxMinusHealthInsurance = incomeTaxMinusHealthInsurance;
        this.finalIncomeTaxValue = finalIncomeTaxValue;
    }

    public static class TaxReportBuilder {

        private BigDecimal incomingVat;
        private BigDecimal outgoingVat;
        private BigDecimal income;
        private BigDecimal costs;
        private BigDecimal incomeMinusCosts;
        private BigDecimal vatToPay;
        private BigDecimal pensionInsurance;
        private BigDecimal incomeMinusCostsMinusPensionInsurance;
        private BigDecimal taxCalculationBase;
        private BigDecimal incomeTax;
        private BigDecimal healthInsurance9;
        private BigDecimal healthInsurance775;
        private BigDecimal incomeTaxMinusHealthInsurance;
        private BigDecimal finalIncomeTaxValue;

        public TaxReportBuilder setIncomingVat(BigDecimal incomingVat) {
            this.incomingVat = incomingVat.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setOutgoingVat(BigDecimal outgoingVat) {
            this.outgoingVat = outgoingVat.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setIncome(BigDecimal income) {
            this.income = income.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setCosts(BigDecimal costs) {
            this.costs = costs.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setIncomeMinusCosts(BigDecimal incomeMinusCosts) {
            this.incomeMinusCosts = incomeMinusCosts.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setVatToPay(BigDecimal vatToPay) {
            this.vatToPay = vatToPay.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setPensionInsurance(BigDecimal pensionInsurance) {
            this.pensionInsurance = pensionInsurance.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setIncomeMinusCostsMinusPensionInsurance(BigDecimal incomeMinusCostsMinusPensionInsurance) {
            this.incomeMinusCostsMinusPensionInsurance = incomeMinusCostsMinusPensionInsurance.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setIncomeTax(BigDecimal incomeTax) {
            this.incomeTax = incomeTax.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setTaxCalculationBase(BigDecimal taxCalculationBase) {
            this.taxCalculationBase = taxCalculationBase.setScale(0, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setHealthInsurance9(BigDecimal healthInsurance9) {
            this.healthInsurance9 = healthInsurance9.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setHealthInsurance775(BigDecimal healthInsurance775) {
            this.healthInsurance775 = healthInsurance775.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setIncomeTaxMinusHealthInsurance(BigDecimal incomeTaxMinusHealthInsurance) {
            this.incomeTaxMinusHealthInsurance = incomeTaxMinusHealthInsurance.setScale(2, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReportBuilder setFinalIncomeTaxValue(BigDecimal finalIncomeTaxValue) {
            this.finalIncomeTaxValue = finalIncomeTaxValue.setScale(0, RoundingMode.HALF_UP);
            return this;
        }

        public TaxReport build() {
            return new TaxReport(this.incomingVat, this.outgoingVat, this.income,
                this.costs, this.incomeMinusCosts, this.vatToPay, this.pensionInsurance,
                this.incomeMinusCostsMinusPensionInsurance, this.taxCalculationBase, this.incomeTax,
                this.healthInsurance9, this.healthInsurance775, this.incomeTaxMinusHealthInsurance, this.finalIncomeTaxValue);
        }
    }
}




