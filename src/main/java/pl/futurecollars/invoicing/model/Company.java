package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
public class Company {


    @Id
    @ApiModelProperty(value = "Tax identification number", required = true, example = "1234567819")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "Ul. Kubusia Puchatka 14A/6, 00-111 Gdańsk")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "XYZ")
    private String name;

    @ApiModelProperty(value = "Company healthy insurance amount", required = true, example = "1000.50")
    private BigDecimal healthyInsurance;

    @ApiModelProperty(value = "Company pension insurance amount", required = true, example = "500.25")
    private BigDecimal pensionInsurance;

}



