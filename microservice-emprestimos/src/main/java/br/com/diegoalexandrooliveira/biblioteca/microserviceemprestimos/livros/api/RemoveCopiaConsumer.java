package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.EventoCopiaLivroRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
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
            Livro livro = livroRepository.findByIsbn(eventoCopiaLivroRecord.getIsbn().toString()).orElseThrow(() -> new IllegalStateException(String.format("Livro com o ISBN %s não foi encotrado, enviando evento para DLQ", eventoCopiaLivroRecord.getIsbn())));
            livro.removeCopia();
            livroRepository.save(livro);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            removeCopiaDLQ.enviar(eventoCopiaLivroRecord);
        }
    }
}
