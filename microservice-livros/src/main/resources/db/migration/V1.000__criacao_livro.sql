create table livro (
id numeric primary key,
titulo character varying(255) not null,
isbn character varying(13) not null,
numero_paginas numeric not null,
ano_lancamento numeric not null,
editora character varying(255) not null,
nome_autor character varying(255) not null,
quantidade_de_copias numeric not null
);

CREATE SEQUENCE livro_sequence START 1;
