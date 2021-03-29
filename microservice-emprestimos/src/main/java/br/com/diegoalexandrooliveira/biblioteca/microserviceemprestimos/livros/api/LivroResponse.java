package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LivroResponse {

    private String isbn;

    private String titulo;

    private int quantidadeCopiasTotal;

    private int quantidadeCopiasDisponiveis;

    public static LivroResponse from(Livro livro) {
        LivroResponse livroResponse = new LivroResponse();
        livroResponse.isbn = livro.getIsbn();
        livroResponse.titulo = livro.getTitulo();
        livroResponse.quantidadeCopiasTotal = livro.getQuantidadeTotal();
        livroResponse.quantidadeCopiasDisponiveis = livro.getQuantidadeDisponivel();
        return livroResponse;
    }
}
