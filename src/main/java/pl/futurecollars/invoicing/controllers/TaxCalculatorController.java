package pl.futurecollars.invoicing.controllers;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.TaxReport;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@Slf4j
@RequiredArgsConstructor
@Api(tags = {"tax-calculator-controller"})
@RestController
public class TaxCalculatorController {

    private final TaxCalculatorService taxCalculatorService;

    @GetMapping(path = "/tax/{taxIdentificationNumber}", produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<TaxReport> getTaxReport(@PathVariable String taxIdentificationNumber) {
        log.debug("Generate tax report for company with Tax Identification Number: " + taxIdentificationNumber);
        try {
            return ResponseEntity.ok()
                .body(taxCalculatorService.taxReport(taxIdentificationNumber));
        } catch (Exception e) {
            log.error(
                "Exception: " + e + " occurred while generating tax report for company with Tax Identification Number: " + taxIdentificationNumber);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}




