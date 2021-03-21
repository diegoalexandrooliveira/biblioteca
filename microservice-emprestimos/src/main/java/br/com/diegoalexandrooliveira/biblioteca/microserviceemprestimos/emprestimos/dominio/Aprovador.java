package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class Aprovador {

    private String usuario;

    private String nome;
}
