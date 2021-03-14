package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class Copia {
    private int quantidadeTotal;

    private int quantidadeDisponivel;
}
