package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LivroTest {


    @DisplayName("Não deve remover uma copia quando não há cópias")
    @Test
    void teste1() {
        Livro livro = new Livro("Livro Teste", "1234567891234", 100, 200, "Editora Teste", "Autor Teste");
        assertThrows(IllegalStateException.class, livro::removeCopia);
    }

    @DisplayName("Deve adicionar uma cópia")
    @Test
    void teste2() {
        Livro livro = new Livro("Livro Teste", "1234567891234", 100, 200, "Editora Teste", "Autor Teste");
        livro.adicionaCopia();
        assertEquals(1, livro.getQuantidadeDeCopias());
    }

}