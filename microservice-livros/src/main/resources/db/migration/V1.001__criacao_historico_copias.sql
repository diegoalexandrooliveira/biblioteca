create table historico_copias (
id numeric primary key,
livro_id numeric not null,
usuario character varying(255) not null,
acao character varying(255) not null,
quantidade_apos_acao numeric not null,
data_hora timestamp with time zone,
FOREIGN KEY (livro_id) REFERENCES livro (id)
);


CREATE SEQUENCE historico_copia_sequence START 1;
