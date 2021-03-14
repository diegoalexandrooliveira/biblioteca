package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import br.com.diegoalexandrooliveira.biblioteca.EventoCopiaLivroRecord;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Livro;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EnviarNovaCopiaLivro {

    private final KafkaTemplate<String, EventoCopiaLivroRecord> kafkaTemplate;
    @Value("${kafka.livros-topics.adiciona-copia}")
    private String topico;


    void enviar(Livro livro) {
        EventoCopiaLivroRecord novaCopiaLivroRecord = EventoCopiaLivroRecord
                .newBuilder()
                .setIsbn(livro.getIsbn())
                .setQuantidadeTotal(livro.getQuantidadeDeCopias())
                .build();
        kafkaTemplate.send(topico, novaCopiaLivroRecord);
    }
}
