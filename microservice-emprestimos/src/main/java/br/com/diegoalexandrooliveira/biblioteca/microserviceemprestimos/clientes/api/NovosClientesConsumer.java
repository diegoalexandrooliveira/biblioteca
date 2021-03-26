package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@RequiredArgsConstructor
public class NovosClientesConsumer {

    private final ClienteRepository clienteRepository;
    private final Servico servico;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    @KafkaListener(groupId = "${kafka.consumer.clientes.group-id}", topics = "${kafka.consumer.clientes.topic}")
    public void consumer(GenericRecord genericRecord, Acknowledgment ack) {
        executorService.submit(servico::enviar);
        ack.acknowledge();
    }
}
