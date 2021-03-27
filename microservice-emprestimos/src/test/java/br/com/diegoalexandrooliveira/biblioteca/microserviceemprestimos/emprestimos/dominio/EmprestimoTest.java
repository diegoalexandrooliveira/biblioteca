package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @DisplayName("Deve criar um emprestimo")
    @Test
    void teste1() {
        Cliente cliente = new Cliente("teste", "Teste Teste", true);
        Aprovador aprovador = new Aprovador("aprovador", "Aprovador");
        Livro livro = Livro
                .builder()
                .anoLancamento(1)
                .editora("Editora")
                .isbn("ISBN")
                .nomeAutor("Autor")
                .numeroPaginas(1)
                .titulo("Titulo")
                .build();

        Emprestimo emprestimo = Emprestimo
                .builder()
                .livros(Set.of(livro))
                .pessoa(cliente)
                .aprovador(aprovador)
                .dataAcordadaDevolucao(ZonedDateTime.now())
                .build();

        assertNotNull(emprestimo.getDataEmprestimo());
        assertEquals(Situacao.ABERTO, emprestimo.getSituacao());
    }

    @DisplayName("Não deve criar um emprestimo porque não possui livros")
    @Test
    void teste2() {
        Emprestimo.Builder builder = Emprestimo.builder();
        Set<Livro> livros = Set.of();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> builder.livros(livros));
        assertEquals("Livros não pode ser vazio.", illegalArgumentException.getMessage());
    }

    @DisplayName("Não deve criar um emprestimo porque cliente está desabilitado")
    @Test
    void teste3() {
        Cliente cliente = new Cliente("teste", "Teste Teste", false);
        Emprestimo.Builder builder = Emprestimo.builder();
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> builder.pessoa(cliente));
        assertEquals("Pessoa Teste Teste não pode realizar emprestimo porque esta desabilitada.", illegalStateException.getMessage());
    }

}