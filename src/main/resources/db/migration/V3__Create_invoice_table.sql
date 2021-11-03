CREATE TABLE public.invoices
(
id uuid NOT NULL UNIQUE,
date date NOT NULL,
issuer varchar(13) NOT NULL,
receiver varchar(13) NOT NULL,
CONSTRAINT issuer_fk FOREIGN KEY(issuer) REFERENCES public.companies(tax_identification_number),
CONSTRAINT receiver_fk FOREIGN KEY(receiver) REFERENCES public.companies(tax_identification_number),
PRIMARY KEY (id)
)