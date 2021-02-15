package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoCopiasRepository extends JpaRepository<HistoricoCopias, Long> {
}
