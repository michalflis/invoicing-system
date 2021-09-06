package pl.futurecollars.invoicing.model;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

    private UUID id;
    private String nip;
    private String address;
    private String name;

    public Company(String nip, String address, String name) {

        this.nip = nip;
        this.address = address;
        this.name = name;
    }
}



