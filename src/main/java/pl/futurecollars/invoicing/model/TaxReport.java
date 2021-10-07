package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxReport {

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal earnings;
    private BigDecimal vatToPay;

    TaxReport(BigDecimal incomingVat, BigDecimal outgoingVat, BigDecimal income,
              BigDecimal costs, BigDecimal earnings, BigDecimal vatToPay) {
        this.incomingVat = incomingVat;
        this.outgoingVat = outgoingVat;
        this.income = income;
        this.costs = costs;
        this.earnings = earnings;
        this.vatToPay = vatToPay;
    }

    public static class TaxReportBuilder {

        private BigDecimal incomingVat;
        private BigDecimal outgoingVat;
        private BigDecimal income;
        private BigDecimal costs;
        private BigDecimal earnings;
        private BigDecimal vatToPay;

        public TaxReportBuilder setIncomingVat(BigDecimal incomingVat) {
            this.incomingVat = incomingVat.setScale(2);
            return this;
        }

        public TaxReportBuilder setOutgoingVat(BigDecimal outgoingVat) {
            this.outgoingVat = outgoingVat.setScale(2);
            return this;
        }

        public TaxReportBuilder setIncome(BigDecimal income) {
            this.income = income.setScale(2);
            return this;
        }

        public TaxReportBuilder setCosts(BigDecimal costs) {
            this.costs = costs.setScale(2);
            return this;
        }

        public TaxReportBuilder setEarnings(BigDecimal earnings) {
            this.earnings = earnings.setScale(2);
            return this;
        }

        public TaxReportBuilder setVatToPay(BigDecimal vatToPay) {
            this.vatToPay = vatToPay.setScale(2);
            return this;
        }

        public TaxReport build() {
            return new TaxReport(this.incomingVat, this.outgoingVat, this.income,
                this.costs, this.earnings, this.vatToPay);
        }
    }
}




