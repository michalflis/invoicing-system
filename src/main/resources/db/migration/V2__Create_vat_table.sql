CREATE TABLE public.vat
(
id serial NOT NULL PRIMARY KEY,
name varchar(10) NOT NULL,
rate numeric(4,2) NOT NULL
);

insert into public.vat (name,rate)
values ('VAT_23',0.23),
 ('VAT_8',0.08),
 ('VAT_5',0.05),
 ('VAT_0',0),
 ('VAT_ZW',0);