package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LivroTest {

    @DisplayName("Deve adicionar copias")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 1_000})
    void teste1(int quantidade) {
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();

        for (int i = 0; i < quantidade; i++) {
            livro.adicionaCopia();
        }

        assertEquals(quantidade, livro.getQuantidadeDisponivel());
        assertEquals(quantidade, livro.getQuantidadeTotal());
    }

}