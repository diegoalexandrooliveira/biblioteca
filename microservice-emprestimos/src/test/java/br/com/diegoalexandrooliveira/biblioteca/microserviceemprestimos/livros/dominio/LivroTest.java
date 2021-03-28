package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @DisplayName("Deve remover copias")
    @Test
    void teste2(){
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();
        livro.adicionaCopia();
        livro.adicionaCopia();
        livro.removeCopia();

        assertEquals(1, livro.getQuantidadeTotal());
        assertEquals(1, livro.getQuantidadeDisponivel());
    }

    @DisplayName("Não deve remover copias porque não há nenhuma")
    @Test
    void teste3(){
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, livro::removeCopia);
        assertEquals("Não é possível remover uma cópia, quantidade total de livros é 0.", illegalStateException.getMessage());
    }


    @DisplayName("Deve realizar emprestimo")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 1_000})
    void teste4(int quantidade) {
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
            livro.realizaEmprestimo();
        }

        assertEquals(0, livro.getQuantidadeDisponivel());
        assertEquals(quantidade, livro.getQuantidadeTotal());
    }

    @DisplayName("Não deve realizar emprestimo porque não há nenhuma cópia disponível")
    @Test
    void teste5(){
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, livro::realizaEmprestimo);
        assertEquals("Não há cópias disponíveis para o livro Titulo", illegalStateException.getMessage());
    }

}