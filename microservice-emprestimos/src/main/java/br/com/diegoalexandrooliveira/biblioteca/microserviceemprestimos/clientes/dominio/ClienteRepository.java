package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, Long> {

    @Query("{'usuario': ?0}")
    Optional<Cliente> procuraPorUsuario(String usuario);
}
