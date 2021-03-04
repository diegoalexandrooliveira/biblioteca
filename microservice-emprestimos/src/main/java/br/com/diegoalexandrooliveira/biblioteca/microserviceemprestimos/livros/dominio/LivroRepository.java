package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends MongoRepository<Livro, Long> {
}
