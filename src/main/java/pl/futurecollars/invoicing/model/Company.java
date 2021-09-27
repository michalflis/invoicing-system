package pl.futurecollars.invoicing.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

    private String nip;
    private String address;
    private String name;

    public Company(String nip, String address, String name) {

        this.nip = nip;
        this.address = address;
        this.name = name;
    }
}



