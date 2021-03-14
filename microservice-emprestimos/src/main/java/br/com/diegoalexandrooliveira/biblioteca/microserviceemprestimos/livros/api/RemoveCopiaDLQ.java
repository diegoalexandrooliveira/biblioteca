package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.EventoCopiaLivroRecord;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveCopiaDLQ {

    private final KafkaTemplate<String, EventoCopiaLivroRecord> kafkaTemplate;
    @Value("${kafka.consumer.livros.remove-copia.dlq:''}")
    private String topico;


    void enviar(GenericRecord genericRecord) {
        EventoCopiaLivroRecord removeCopiaRecord = EventoCopiaLivroRecord
                .newBuilder()
                .setIsbn(genericRecord.get("isbn").toString())
                .setQuantidadeTotal(Integer.parseInt(genericRecord.get("quantidadeTotal").toString()))
                .build();
        kafkaTemplate.send(topico, removeCopiaRecord);
    }
}
