package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import br.com.diegoalexandrooliveira.biblioteca.LivroRecord;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Livro;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EnviarNovoLivro {

    private final KafkaTemplate<String, LivroRecord> kafkaTemplate;
    @Value("${kafka.livros-topics.novos}")
    private String topico;


    void enviar(Livro livro) {
        LivroRecord livroRecord = LivroRecord
                .newBuilder()
                .setTitulo(livro.getTitulo())
                .setIsbn(livro.getIsbn())
                .setNomeAutor(livro.getNomeAutor())
                .setEditora(livro.getEditora())
                .setNumeroPaginas(livro.getNumeroPaginas())
                .setAnoLancamento(livro.getAnoLancamento())
                .build();
        kafkaTemplate.send(topico, livroRecord);
    }
}
