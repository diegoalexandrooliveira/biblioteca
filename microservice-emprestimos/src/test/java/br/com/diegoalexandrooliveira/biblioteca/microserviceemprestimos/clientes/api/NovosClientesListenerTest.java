package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class NovosClientesListenerTest {

    @Autowired
    private NovosClientesListener novosClientesListener;
    @Autowired
    private ClienteRepository clienteRepository;

    @DisplayName("Deve salvar cliente")
    @Test
    void teste1() {
        GenericRecord genericRecord = mock(GenericRecord.class);

        when(genericRecord.get("usuario")).thenReturn("usuario_teste");
        when(genericRecord.get("nomeCompleto")).thenReturn("Usuário de Teste");
        when(genericRecord.get("habilitado")).thenReturn("true");
        novosClientesListener.consumer(genericRecord);

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

        GenericRecord genericRecord = mock(GenericRecord.class);

        when(genericRecord.get("usuario")).thenReturn("usuario_teste2");
        when(genericRecord.get("nomeCompleto")).thenReturn("Usuário Atualizado");
        when(genericRecord.get("habilitado")).thenReturn("false");

        novosClientesListener.consumer(genericRecord);

        Cliente clienteRecuperado = clienteRepository.findById(cliente.getId()).orElseThrow();

        assertEquals("usuario_teste2", clienteRecuperado.getUsuario());
        assertEquals("Usuário Atualizado", clienteRecuperado.getNomeCompleto());
        assertFalse(clienteRecuperado.isHabilitado());
        assertEquals(0, clienteRecuperado.getQuantidadeEmprestimos());
    }


}
