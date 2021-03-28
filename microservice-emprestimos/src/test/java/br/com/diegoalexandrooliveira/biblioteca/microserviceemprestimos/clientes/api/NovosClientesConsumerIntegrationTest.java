package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import br.com.diegoalexandrooliveira.biblioteca.ClienteRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class NovosClientesConsumerIntegrationTest {

    @Autowired
    private NovosClientesConsumer novosClientesConsumer;
    @Autowired
    private ClienteRepository clienteRepository;

    @DisplayName("Deve salvar cliente")
    @Test
    void teste1() {

        ClienteRecord clienteRecord = ClienteRecord.newBuilder()
                .setUsuario("usuario_teste")
                .setNomeCompleto("Usuário de Teste")
                .setHabilitado(true)
                .setCpf("123")
                .setCidade("123")
                .setEstado("123")
                .setLogradouro("123")
                .setNumero(1)
                .build();

        novosClientesConsumer.consumer(clienteRecord);

        Cliente cliente = clienteRepository.procuraPorUsuario("usuario_teste").orElseThrow();

        assertEquals("usuario_teste", cliente.getUsuario());
        assertEquals("Usuário de Teste", cliente.getNomeCompleto());
        assertTrue(cliente.isHabilitado());
        assertEquals(0, cliente.getQuantidadeEmprestimos());
    }

    @DisplayName("Deve atualizar cliente")
    @Test
    void teste2() {
        Cliente cliente = new Cliente("usuario_teste2", "Usuario de teste", true);

        clienteRepository.save(cliente);

        ClienteRecord clienteRecord = ClienteRecord.newBuilder()
                .setUsuario("usuario_teste2")
                .setNomeCompleto("Usuário Atualizado")
                .setHabilitado(false)
                .setCpf("123")
                .setCidade("123")
                .setEstado("123")
                .setLogradouro("123")
                .setNumero(1)
                .build();

        novosClientesConsumer.consumer(clienteRecord);

        Cliente clienteRecuperado = clienteRepository.findById(new ObjectId(cliente.getId())).orElseThrow();

        assertEquals("usuario_teste2", clienteRecuperado.getUsuario());
        assertEquals("Usuário Atualizado", clienteRecuperado.getNomeCompleto());
        assertFalse(clienteRecuperado.isHabilitado());
        assertEquals(0, clienteRecuperado.getQuantidadeEmprestimos());
    }
}
