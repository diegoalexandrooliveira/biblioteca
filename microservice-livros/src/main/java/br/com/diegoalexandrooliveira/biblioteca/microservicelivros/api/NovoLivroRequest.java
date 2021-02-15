package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.comum.UnicoValor;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class NovoLivroRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    @UnicoValor(campoUnico = "isbn", entidade = "Livro")
    @ISBN
    private String isbn;

    @Min(1)
    private int numeroPaginas;

    @Min(1000)
    private int anoLancamento;

    @NotBlank
    private String editora;

    @NotBlank
    private String nomeAutor;

    Livro converte() {
        return new Livro(titulo, isbn, numeroPaginas, anoLancamento, editora, nomeAutor);
    }

}
