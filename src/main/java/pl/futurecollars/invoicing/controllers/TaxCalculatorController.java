package pl.futurecollars.invoicing.controllers;

import io.swagger.annotations.Api;
import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.TaxReport;
import pl.futurecollars.invoicing.service.TaxCalculatorService;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@RequiredArgsConstructor
@Api(tags = {"tax-calculator-controller"})
@RestController
public class TaxCalculatorController {

    private final TaxCalculatorService taxCalculatorService;

    @GetMapping(path = "/tax/{taxIdentificationNumber}", produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<TaxReport> getTaxReport(@PathVariable String taxIdentificationNumber) {
        log.debug("Generate tax report for company with Tax Identification Number: " + taxIdentificationNumber);
        return ResponseEntity.ok()
            .body(taxCalculatorService.taxReport(taxIdentificationNumber));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Json> handleError(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        return ResponseEntity.badRequest().lastModified(ZonedDateTime.now()).build();
    }
}




