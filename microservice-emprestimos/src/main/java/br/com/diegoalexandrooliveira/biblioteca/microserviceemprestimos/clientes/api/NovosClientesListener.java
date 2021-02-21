package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@KafkaListener(groupId = "${kafka.consumer.topic-clientes-group-id}", topics = "${kafka.consumer.topic-clientes}")
@Component
@RequiredArgsConstructor
class NovosClientesListener {

    private final ClienteRepository clienteRepository;

    @KafkaHandler
    protected void consumer(@Payload GenericRecord genericRecord) {
        Cliente cliente = clienteRepository
                .procuraPorUsuario(genericRecord.get("usuario").toString())
                .map(clienteEnconrado -> {
                    clienteEnconrado.alteraDadosPessoais(genericRecord.get("nomeCompleto").toString(), Boolean.parseBoolean(genericRecord.get("habilitado").toString()));
                    return clienteEnconrado;
                })
                .orElse(new Cliente(genericRecord.get("usuario").toString(),
                        genericRecord.get("nomeCompleto").toString(),
                        Boolean.parseBoolean(genericRecord.get("habilitado").toString())));

        clienteRepository.save(cliente);
    }

}
