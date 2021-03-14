package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends MongoRepository<Livro, Long> {

    @Query("{'isbn': ?0}")
    Optional<Livro> findByIsbn(String isbn);
}
