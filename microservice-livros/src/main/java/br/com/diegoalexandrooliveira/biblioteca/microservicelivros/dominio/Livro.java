package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Livro {

    @Id
    @GeneratedValue(generator = "livro_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "livro_sequence", allocationSize = 1)
    private Long id;

    private String titulo;

    private String isbn;

    private int numeroPaginas;

    private int anoLancamento;

    private String editora;

    private String nomeAutor;

    private int quantidadeDeCopias;


    public Livro(@NonNull String titulo, @NonNull String isbn, @NonNull int numeroPaginas, @NonNull int anoLancamento, @NonNull String editora, @NonNull String nomeAutor) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.anoLancamento = anoLancamento;
        this.editora = editora;
        this.nomeAutor = nomeAutor;
        this.quantidadeDeCopias = 0;
    }

    public void adicionaCopia() {
        this.quantidadeDeCopias++;
    }

    public void removeCopia() {
        if (quantidadeDeCopias == 0) {
            throw new IllegalStateException("Não há cópias a serem removidas.");
        }
        this.quantidadeDeCopias--;
    }
}
