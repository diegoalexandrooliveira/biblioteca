package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.ThreadLocalRandom;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Livro {

    @Id
    private Long id;

    private String titulo;

    private String isbn;

    private String editora;

    private String nomeAutor;

    private int numeroPaginas;

    private int anoLancamento;

    public static LivroBuilder builder() {
        return new LivroBuilder();
    }

    public static final class LivroBuilder {
        private String titulo;
        private String isbn;
        private String editora;
        private String nomeAutor;
        private int numeroPaginas;
        private int anoLancamento;

        private LivroBuilder() {
        }

        public LivroBuilder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public LivroBuilder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public LivroBuilder editora(String editora) {
            this.editora = editora;
            return this;
        }

        public LivroBuilder nomeAutor(String nomeAutor) {
            this.nomeAutor = nomeAutor;
            return this;
        }

        public LivroBuilder numeroPaginas(int numeroPaginas) {
            this.numeroPaginas = numeroPaginas;
            return this;
        }

        public LivroBuilder anoLancamento(int anoLancamento) {
            this.anoLancamento = anoLancamento;
            return this;
        }

        public Livro build() {
            Livro livro = new Livro();
            livro.editora = this.editora;
            livro.isbn = this.isbn;
            livro.titulo = this.titulo;
            livro.numeroPaginas = this.numeroPaginas;
            livro.nomeAutor = this.nomeAutor;
            livro.anoLancamento = this.anoLancamento;
            livro.id = ThreadLocalRandom.current().nextLong(1_000_000);
            return livro;
        }
    }
}
