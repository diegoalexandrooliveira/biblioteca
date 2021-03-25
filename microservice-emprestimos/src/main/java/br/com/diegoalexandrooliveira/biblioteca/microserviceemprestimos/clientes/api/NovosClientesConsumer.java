package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import br.com.diegoalexandrooliveira.biblioteca.ClienteRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@KafkaListener(groupId = "${kafka.consumer.clientes.group-id}", topics = "${kafka.consumer.clientes.topic}")
public class NovosClientesConsumer {

    private final ClienteRepository clienteRepository;

    @KafkaHandler
    public void consumer(ClienteRecord clienteRecord) {
        Cliente cliente = clienteRepository
                .procuraPorUsuario(clienteRecord.getUsuario().toString())
                .map(clienteEnconrado -> {
                    clienteEnconrado.alteraDadosPessoais(clienteRecord.getNomeCompleto().toString(), clienteRecord.getHabilitado());
                    return clienteEnconrado;
                })
                .orElse(new Cliente(clienteRecord.getUsuario().toString(),
                        clienteRecord.getNomeCompleto().toString(),
                        clienteRecord.getHabilitado()));

        clienteRepository.save(cliente);
    }
}
