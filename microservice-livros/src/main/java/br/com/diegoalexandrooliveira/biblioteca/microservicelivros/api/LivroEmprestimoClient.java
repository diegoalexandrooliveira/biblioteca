package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;

import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security.LivrosEmprestimoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "livrosEmprestimo", url = "${emprestimo.service-url}", configuration = LivrosEmprestimoConfiguration.class)
public interface LivroEmprestimoClient {

    @GetMapping(path = "/livro/isbn/{isbn}")
    LivroEmprestimoResponse procuraPorIsbn(@PathVariable("isbn") String isbn);
}
