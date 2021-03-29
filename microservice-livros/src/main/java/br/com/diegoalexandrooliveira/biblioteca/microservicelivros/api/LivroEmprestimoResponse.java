package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LivroEmprestimoResponse {

    private String isbn;

    private String titulo;

    private int quantidadeCopiasTotal;

    private int quantidadeCopiasDisponiveis;
}
