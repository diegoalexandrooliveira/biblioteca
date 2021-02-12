create table cliente (
id numeric primary key,
usuario character varying(255) not null,
nome_completo character varying(255) not null,
cpf character varying(11) not null,
logradouro character varying(255) not null,
numero numeric not null,
cidade character varying(11) not null,
estado character varying(11) not null,
habilitado boolean default 'true'
);

CREATE SEQUENCE cliente_sequence START 1;
