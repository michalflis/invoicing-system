package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

@AllArgsConstructor
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
@Service
public class SqlDatabase implements Database {

    private final JdbcTemplate jdbcTemplate;

    private Vat stringToVat(String string) {
        switch (string) {
            case "VAT_23":
                return Vat.VAT_23;
            case "VAT_8":
                return Vat.VAT_8;
            case "VAT_5":
                return Vat.VAT_5;
            case "VAT_0":
                return Vat.VAT_0;
            case "VAT_ZW":
                return Vat.VAT_ZW;
            default:
                break;
        }
        return null;
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {

        jdbcTemplate.update("insert into companies (name, address, tax_identification_number, healthy_insurance, pension_insurance) "
                + "values (?, ?, ?, ?, ?);",
            invoice.getReceiver().getName(),
            invoice.getReceiver().getAddress(),
            invoice.getReceiver().getTaxIdentificationNumber(),
            invoice.getReceiver().getHealthyInsurance(),
            invoice.getReceiver().getPensionInsurance()
        );

        jdbcTemplate.update("insert into companies (name, address, tax_identification_number, healthy_insurance, pension_insurance) "
                + "values (?, ?, ?, ?, ?);",
            invoice.getIssuer().getName(),
            invoice.getIssuer().getAddress(),
            invoice.getIssuer().getTaxIdentificationNumber(),
            invoice.getIssuer().getHealthyInsurance(),
            invoice.getIssuer().getPensionInsurance()
        );

        UUID invoiceId = UUID.randomUUID();
        invoice.setId(invoiceId);

        jdbcTemplate.update("insert into invoices (date, receiver, issuer, id) values (?, ?, ?, ?);",
            Date.valueOf(invoice.getDate()),
            invoice.getReceiver().getTaxIdentificationNumber(),
            invoice.getIssuer().getTaxIdentificationNumber(),
            invoice.getId()
        );

        for (InvoiceEntry invoiceEntry : invoice.getEntries()) {

            BigDecimal vatValue = (invoiceEntry.getPrice()
                .multiply(new BigDecimal(Float.toString(invoiceEntry.getVatRate().getRate()))))
                .setScale(2, RoundingMode.HALF_UP);
            invoiceEntry.setVatValue(vatValue);

            jdbcTemplate.update("insert into invoice_entries "
                    + "(description, price, vat_value, vat_rate, car_used_for_personal_reason, "
                    + "car_registration_number, invoice_id) "
                    + "values (?, ?, ?, ?, ?, ?, ?);",
                invoiceEntry.getDescription(),
                invoiceEntry.getPrice().setScale(2, RoundingMode.HALF_UP),
                invoiceEntry.getVatValue().setScale(2, RoundingMode.HALF_UP),
                invoiceEntry.getVatRate().toString(),
                invoiceEntry.getCarUsedForPersonalReason(),
                invoiceEntry.getCarRegistrationNumber(),
                invoice.getId()
            );
        }
        return invoice;
    }

    @Override
    @Transactional
    public Invoice getById(UUID id) {
        return getAll().stream().filter(invoice -> invoice.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    @Override
    @Transactional
    public List<Invoice> getAll() {
        return jdbcTemplate.query("select i.id, i.date, "
                + "c1.name as issuer_name, c1.tax_identification_number as issuer_tax_id, c1.address as issuer_address, "
                + "c1.pension_insurance as issuer_pension_insurance, c1.healthy_insurance as issuer_healthy_insurance, "
                + "c2.name as receiver_name, c2.tax_identification_number as receiver_tax_id, c2.address as receiver_address, "
                + "c2.pension_insurance as receiver_pension_insurance, c2.healthy_insurance as receiver_healthy_insurance "
                + "from invoices i "
                + "inner join companies c1 on i.issuer = c1.tax_identification_number "
                + "inner join companies c2 on i.receiver = c2.tax_identification_number",
            (rs, rowNr) -> {
                UUID invoiceId = rs.getObject("id", UUID.class);
                List<InvoiceEntry> invoiceEntries = jdbcTemplate.query("select * from invoices i "
                        + "inner join invoice_entries e on i.id = e.invoice_id where i.id = '"
                        + invoiceId
                        + "' order by e.id",
                    (response, ignored) -> InvoiceEntry.builder()
                        .description(response.getString("description"))
                        .price(response.getBigDecimal("price").setScale(2, RoundingMode.HALF_UP))
                        .vatValue(response.getBigDecimal("vat_value").setScale(2, RoundingMode.HALF_UP))
                        .vatRate(stringToVat(response.getString("vat_rate")))
                        .carRegistrationNumber(response.getString("car_registration_number"))
                        .carUsedForPersonalReason(response.getBoolean("car_used_for_personal_reason"))
                        .build());

                return Invoice.builder()
                    .date(rs.getDate("date").toLocalDate())
                    .id(UUID.fromString(rs.getString("id")))
                    .issuer(Company.builder()
                        .name(rs.getString("issuer_name"))
                        .address(rs.getString("issuer_address"))
                        .taxIdentificationNumber(rs.getString("issuer_tax_id"))
                        .healthyInsurance(rs.getBigDecimal("issuer_healthy_insurance").setScale(2, RoundingMode.HALF_UP))
                        .pensionInsurance(rs.getBigDecimal("issuer_pension_insurance").setScale(2, RoundingMode.HALF_UP))
                        .build())
                    .receiver(Company.builder()
                        .name(rs.getString("receiver_name"))
                        .address(rs.getString("receiver_address"))
                        .taxIdentificationNumber(rs.getString("receiver_tax_id"))
                        .healthyInsurance(rs.getBigDecimal("receiver_healthy_insurance").setScale(2, RoundingMode.HALF_UP))
                        .pensionInsurance(rs.getBigDecimal("receiver_pension_insurance").setScale(2, RoundingMode.HALF_UP))
                        .build())
                    .entries(invoiceEntries)
                    .build();
            });
    }

    @Override
    @Transactional
    public Invoice update(Invoice updatedInvoice) {
        jdbcTemplate.update("update companies"
                + " set name = ?, address = ?, healthy_insurance = ?, pension_insurance= ?"
                + " where tax_identification_number = ?",
            updatedInvoice.getReceiver().getName(),
            updatedInvoice.getReceiver().getAddress(),
            updatedInvoice.getReceiver().getHealthyInsurance(),
            updatedInvoice.getReceiver().getPensionInsurance(),
            updatedInvoice.getReceiver().getTaxIdentificationNumber()
        );

        jdbcTemplate.update("update companies"
                + " set name = ?, address = ?, healthy_insurance = ?, pension_insurance= ?"
                + " where tax_identification_number = ?",
            updatedInvoice.getIssuer().getName(),
            updatedInvoice.getIssuer().getAddress(),
            updatedInvoice.getIssuer().getHealthyInsurance(),
            updatedInvoice.getIssuer().getPensionInsurance(),
            updatedInvoice.getIssuer().getTaxIdentificationNumber()
        );

        jdbcTemplate.update("update invoices"
                + " set date = ?, receiver = ?, issuer = ?"
                + " where id = ?",
            Date.valueOf(updatedInvoice.getDate()),
            updatedInvoice.getReceiver().getTaxIdentificationNumber(),
            updatedInvoice.getIssuer().getTaxIdentificationNumber(),
            updatedInvoice.getId()
        );

        for (InvoiceEntry invoiceEntry : updatedInvoice.getEntries()) {

            BigDecimal vatValue = (invoiceEntry.getPrice()
                .multiply(new BigDecimal(Float.toString(invoiceEntry.getVatRate().getRate()))))
                .setScale(2, RoundingMode.HALF_UP);
            invoiceEntry.setVatValue(vatValue);

            jdbcTemplate.update("update invoice_entries"
                    + " set description = ?, price = ?, vat_value = ?, vat_rate = ?, car_used_for_personal_reason = ?,"
                    + " car_registration_number = ?"
                    + " where invoice_id = ? and id = ?",
                invoiceEntry.getDescription(),
                invoiceEntry.getPrice(),
                invoiceEntry.getVatValue(),
                invoiceEntry.getVatRate().toString(),
                invoiceEntry.getCarUsedForPersonalReason(),
                invoiceEntry.getCarRegistrationNumber(),
                updatedInvoice.getId(),
                invoiceEntry.getId()
            );
        }
        return updatedInvoice;
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        if (id != null) {
            return jdbcTemplate.query("select * from invoices where id = '"
                + id
                + "';", rs -> {
                    if (rs.next()) {
                        jdbcTemplate.execute("delete from invoice_entries where invoice_id = '"
                            + id
                            + "';");
                        jdbcTemplate.execute("delete from invoices where id = '"
                            + id
                            + "';");
                        return true;
                    }
                    return false;
                });
        }
        return false;
    }

    @Override
    public void clear() {
        jdbcTemplate.execute("delete from invoice_entries");
        jdbcTemplate.execute("delete from invoices");
        jdbcTemplate.execute("delete from companies");
    }
}
