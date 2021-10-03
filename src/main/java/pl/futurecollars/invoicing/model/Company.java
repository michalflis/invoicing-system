package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

    @ApiModelProperty(value = "Tax identification number", required = true, example = "123-45-67-819")
    private String nip;

    @ApiModelProperty(value = "Company address", required = true, example = "Ul. Kubusia Puchatka 14A/6, 00-111 Gdańsk")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "XYZ")
    private String name;

    public Company(String nip, String address, String name) {

        this.nip = nip;
        this.address = address;
        this.name = name;
    }
}



