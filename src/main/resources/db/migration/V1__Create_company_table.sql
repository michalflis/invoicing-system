CREATE TABLE public.companies
(
tax_identification_number varchar(13) NOT NULL PRIMARY KEY,
address varchar(200) NOT NULL,
name varchar(100) NOT NULL,
healthy_insurance numeric(10,2) NOT NULL DEFAULT 0,
pension_insurance numeric(10,2) NOT NULL DEFAULT 0
)