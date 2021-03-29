package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "isbn")
public class Livro {

    @Id
    private ObjectId id;

    private String titulo;

    private String isbn;

    private String editora;

    private String nomeAutor;

    private int numeroPaginas;

    private int anoLancamento;

    private Copia copias;

    public String getId() {
        return id.toHexString();
    }

    public static LivroBuilder builder() {
        return new LivroBuilder();
    }

    public void adicionaCopia() {
        if (Objects.isNull(copias)) {
            copias = new Copia(0, 0);
        }
        int quantidadeTotal = copias.getQuantidadeTotal();
        int quantidadeDisponivel = copias.getQuantidadeDisponivel();
        copias = new Copia(++quantidadeTotal, ++quantidadeDisponivel);
    }

    public void removeCopia() {
        if (Objects.isNull(copias)) {
            copias = new Copia(0, 0);
        }
        if (copias.getQuantidadeTotal() == 0) {
            throw new IllegalStateException("Não é possível remover uma cópia, quantidade total de livros é 0.");
        }
        int quantidadeTotal = copias.getQuantidadeTotal();
        int quantidadeDisponivel = copias.getQuantidadeDisponivel();
        copias = new Copia(--quantidadeTotal, --quantidadeDisponivel);
    }

    public void realizaEmprestimo() {
        if (Objects.isNull(copias)) {
            copias = new Copia(0, 0);
        }
        int quantidadeDisponivel = copias.getQuantidadeDisponivel();
        if (quantidadeDisponivel == 0) {
            throw new IllegalStateException(String.format("Não há cópias disponíveis para o livro %s", this.titulo));
        }
        copias = new Copia(copias.getQuantidadeTotal(), --quantidadeDisponivel);
    }

    public int getQuantidadeDisponivel() {
        return copias.getQuantidadeDisponivel();
    }

    public int getQuantidadeTotal() {
        return copias.getQuantidadeTotal();
    }

    public void devolverCopia() {
        int quantidadeDisponivel = copias.getQuantidadeDisponivel();
        copias = new Copia(copias.getQuantidadeTotal(), ++quantidadeDisponivel);
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
            livro.id = new ObjectId();
            livro.copias = new Copia(0, 0);
            return livro;
        }
    }
}
