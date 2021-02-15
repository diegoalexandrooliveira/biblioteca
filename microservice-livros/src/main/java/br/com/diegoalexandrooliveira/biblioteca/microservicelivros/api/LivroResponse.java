package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LivroResponse {

    private Long id;

    private String titulo;

    private String isbn;

    private int numeroPaginas;

    private int anoLancamento;

    private String editora;

    private String nomeAutor;

    private int quantidadeDeCopias;


    public static LivroResponse of(Livro livro) {
        LivroResponse livroResponse = new LivroResponse();
        livroResponse.id = livro.getId();
        livroResponse.titulo = livro.getTitulo();
        livroResponse.isbn = livro.getIsbn();
        livroResponse.numeroPaginas = livro.getNumeroPaginas();
        livroResponse.anoLancamento = livro.getAnoLancamento();
        livroResponse.editora = livro.getEditora();
        livroResponse.nomeAutor = livro.getNomeAutor();
        livroResponse.quantidadeDeCopias = livro.getQuantidadeDeCopias();
        return livroResponse;
    }
}
