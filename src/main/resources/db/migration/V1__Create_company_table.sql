CREATE TABLE public.companies
(
id serial NOT NULL PRIMARY KEY,
tax_identification_number varchar(10) NOT NULL,
address varchar(200) NOT NULL,
name varchar(100) NOT NULL,
healthy_insurance numeric(10,2) NOT NULL DEFAULT 0,
pension_insurance numeric(10,2) NOT NULL DEFAULT 0
)