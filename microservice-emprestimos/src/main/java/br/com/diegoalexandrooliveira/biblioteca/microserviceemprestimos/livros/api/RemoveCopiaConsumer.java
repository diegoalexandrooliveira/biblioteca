package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.EventoCopiaLivroRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@KafkaListener(groupId = "${kafka.consumer.livros.remove-copia.group-id}", topics = "${kafka.consumer.livros.remove-copia.topic}")
@Component
@RequiredArgsConstructor
@Slf4j
class RemoveCopiaConsumer {

    private final LivroRepository livroRepository;
    private final RemoveCopiaDLQ removeCopiaDLQ;

    @KafkaHandler
    protected void consumer(EventoCopiaLivroRecord eventoCopiaLivroRecord) {
        try {
            Livro livro = livroRepository.findByIsbn(eventoCopiaLivroRecord.getIsbn().toString()).orElseThrow(IllegalStateException::new);
            livro.removeCopia();
            livroRepository.save(livro);
        } catch (IllegalStateException e) {
            log.error("Livro com o ISBN {} não foi encotrado, enviando evento para DLQ", eventoCopiaLivroRecord.getIsbn());
            removeCopiaDLQ.enviar(eventoCopiaLivroRecord);
        }
    }
}
