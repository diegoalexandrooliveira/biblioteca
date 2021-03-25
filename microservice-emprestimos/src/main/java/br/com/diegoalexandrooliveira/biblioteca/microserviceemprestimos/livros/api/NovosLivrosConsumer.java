package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.LivroRecord;
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
    protected void consumer(LivroRecord livroRecord) {
        Livro livro = Livro.builder()
                .titulo(livroRecord.getTitulo().toString())
                .isbn(livroRecord.getIsbn().toString())
                .editora(livroRecord.getEditora().toString())
                .nomeAutor(livroRecord.getNomeAutor().toString())
                .numeroPaginas(livroRecord.getNumeroPaginas())
                .anoLancamento(livroRecord.getAnoLancamento())
                .build();
        livroRepository.save(livro);
    }
}
