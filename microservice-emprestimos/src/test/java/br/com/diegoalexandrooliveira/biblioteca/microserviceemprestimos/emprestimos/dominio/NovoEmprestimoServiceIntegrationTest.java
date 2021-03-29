package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api.NovoEmprestimoRequest;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void cleanup(){
        clienteRepository.deleteAll();
        livroRepository.deleteAll();
        emprestimoRepository.deleteAll();
    }

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

    @DisplayName("Não deve realizar um emprestimo porque não existe o aprovador informado")
    @Test
    void teste2() {
        ZonedDateTime dataParaDevolucao = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        NovoEmprestimoRequest novoEmprestimoRequest = new NovoEmprestimoRequest("cliente", Set.of("ISBN"), dataParaDevolucao);

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, "aprovador"));

        assertEquals("Usuário aprovador não encontrado", illegalArgumentException.getMessage());

    }

    @DisplayName("Não deve realizar um emprestimo porque não existe o cliente informado")
    @Test
    void teste3() {
        Cliente aprovador = new Cliente("aprovador", "Aprovador", true);
        clienteRepository.save(aprovador);

        ZonedDateTime dataParaDevolucao = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        NovoEmprestimoRequest novoEmprestimoRequest = new NovoEmprestimoRequest("cliente", Set.of("ISBN"), dataParaDevolucao);

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, "aprovador"));

        assertEquals("Cliente cliente não encontrado.", illegalArgumentException.getMessage());

    }

    @DisplayName("Não deve realizar um emprestimo porque não existe o livro informado")
    @Test
    void teste4() {
        Cliente aprovador = new Cliente("aprovador", "Aprovador", true);
        Cliente cliente = new Cliente("cliente", "Cliente", true);
        clienteRepository.save(aprovador);
        clienteRepository.save(cliente);

        ZonedDateTime dataParaDevolucao = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        NovoEmprestimoRequest novoEmprestimoRequest = new NovoEmprestimoRequest("cliente", Set.of("LIVRO"), dataParaDevolucao);

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, "aprovador"));

        assertEquals("ISBN LIVRO não encontrado.", illegalArgumentException.getMessage());
    }

    @DisplayName("Não deve realizar um emprestimo porque não há cópias do livro informado")
    @Test
    void teste5() {
        Livro livro = Livro.builder()
                .titulo("Titulo")
                .numeroPaginas(1)
                .nomeAutor("Autor")
                .isbn("ISBN")
                .editora("Editora")
                .anoLancamento(1)
                .build();
        livroRepository.save(livro);

        Cliente aprovador = new Cliente("aprovador", "Aprovador", true);
        Cliente cliente = new Cliente("cliente", "Cliente", true);
        clienteRepository.save(aprovador);
        clienteRepository.save(cliente);

        ZonedDateTime dataParaDevolucao = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        NovoEmprestimoRequest novoEmprestimoRequest = new NovoEmprestimoRequest("cliente", Set.of("ISBN"), dataParaDevolucao);

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, "aprovador"));

        assertEquals("Não há cópias disponíveis para o livro Titulo", illegalStateException.getMessage());
    }

}