package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@KafkaListener(groupId = "${kafka.consumer.livros.novos.group-id}", topics = "${kafka.consumer.livros.novos.topic}")
@Component
@RequiredArgsConstructor
class NovosLivrosConsumer {

    private final LivroRepository livroRepository;

    @KafkaHandler
    protected void consumer(@Payload GenericRecord genericRecord) {
        Livro livro = Livro.builder()
                .titulo(genericRecord.get("titulo").toString())
                .isbn(genericRecord.get("isbn").toString())
                .editora(genericRecord.get("editora").toString())
                .nomeAutor(genericRecord.get("nomeAutor").toString())
                .numeroPaginas(Integer.parseInt(genericRecord.get("numeroPaginas").toString()))
                .anoLancamento(Integer.parseInt(genericRecord.get("anoLancamento").toString()))
                .build();
        livroRepository.save(livro);
    }
}
