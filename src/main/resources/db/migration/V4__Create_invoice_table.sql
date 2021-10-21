CREATE TABLE public.invoices
(
id serial NOT NULL PRIMARY KEY,
date date NOT NULL,
issuer int NOT NULL,
receiver int NOT NULL,
invoice_entry int NOT NULL,
CONSTRAINT issuer_fk FOREIGN KEY(issuer) REFERENCES public.companies(id),
CONSTRAINT receiver_fk FOREIGN KEY(receiver) REFERENCES public.companies(id),
CONSTRAINT invoice_entry_fk FOREIGN KEY(invoice_entry) REFERENCES public.invoice_entries(id)
)