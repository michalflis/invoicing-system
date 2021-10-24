CREATE TABLE public.invoice_entries
(
id serial NOT NULL PRIMARY KEY,
description varchar(200) NOT NULL,
price numeric(10,2) NOT NULL DEFAULT 0,
vat_value numeric(10,2) NOT NULL DEFAULT 0,
vat_rate int NOT NULL,
car_used_for_personal_reason boolean NOT NULL DEFAULT FALSE,
car_registration_number varchar(20) NOT NULL,
CONSTRAINT vat_rate_fk FOREIGN KEY(vat_rate) REFERENCES public.vat(id)
);

