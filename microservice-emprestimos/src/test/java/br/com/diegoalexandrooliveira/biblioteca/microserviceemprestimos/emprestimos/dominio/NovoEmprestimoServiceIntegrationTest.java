package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api.NovoEmprestimoRequest;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class NovoEmprestimoServiceIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private NovoEmprestimoService novoEmprestimoService;

    @DisplayName("Deve realizar um emprestimo")
    @Test
    void teste1() {
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();
        livro.adicionaCopia();
        livroRepository.save(livro);

        Cliente aprovador = new Cliente("aprovador", "Aprovador", true);
        Cliente cliente = new Cliente("cliente", "Cliente", true);
        clienteRepository.save(aprovador);
        clienteRepository.save(cliente);

        ZonedDateTime dataParaDevolucao = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        NovoEmprestimoRequest novoEmprestimoRequest = new NovoEmprestimoRequest("cliente", Set.of("ISBN"), dataParaDevolucao);

        String idEmprestimo = novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, "aprovador").getId();

        Livro livroAposEmprestimo = livroRepository.findByIsbn("ISBN").orElseThrow();
        Emprestimo emprestimo = emprestimoRepository.findById(new ObjectId(idEmprestimo)).orElseThrow();

        assertEquals(1, livroAposEmprestimo.getQuantidadeTotal());
        assertEquals(0, livroAposEmprestimo.getQuantidadeDisponivel());
        assertNotNull(emprestimo);
        assertEquals(dataParaDevolucao, emprestimo.getDataAcordadaDevolucao());
        assertNotNull(emprestimo.getDataEmprestimo());
        assertEquals(Situacao.ABERTO, emprestimo.getSituacao());
        assertEquals(cliente, emprestimo.getPessoa());
        assertEquals(1, emprestimo.getLivros().size());
        assertTrue(emprestimo.getLivros().contains(livroAposEmprestimo));
    }

}