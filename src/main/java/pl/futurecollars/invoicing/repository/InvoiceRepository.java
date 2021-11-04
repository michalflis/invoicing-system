package pl.futurecollars.invoicing.repository;

import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, UUID> {

}
