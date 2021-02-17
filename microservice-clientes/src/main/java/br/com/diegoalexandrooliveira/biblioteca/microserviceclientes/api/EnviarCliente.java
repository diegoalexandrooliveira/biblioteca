package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.ClienteRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EnviarCliente {

    private final KafkaTemplate<String, ClienteRecord> kafkaTemplate;
    @Value("${kafka.producer.topic}")
    private String topico;


    void enviar(Cliente cliente) {
        ClienteRecord clienteRecord = ClienteRecord.newBuilder()
                .setUsuario(cliente.getUsuario())
                .setCpf(cliente.getCpf())
                .setNomeCompleto(cliente.getNomeCompleto())
                .setLogradouro(cliente.getLogradouro())
                .setNumero(cliente.getNumero())
                .setCidade(cliente.getCidade())
                .setEstado(cliente.getEstado())
                .setHabilitado(cliente.isHabilitado())
                .build();
        kafkaTemplate.send(topico, clienteRecord);
    }
}
