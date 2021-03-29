package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;

import static br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.config.security.Papeis.ADMIN;

@RestController
@RequestMapping(path = "/emprestimos/admin/livro")
@RequiredArgsConstructor
public class LivrosController {

    private final LivroRepository livroRepository;

    @GetMapping("/isbn/{isbn}")
    @RolesAllowed(ADMIN)
    public ResponseEntity<LivroResponse> procuraPorIsbn(@PathVariable("isbn") String isbn) {
        Livro livro = livroRepository.findByIsbn(isbn).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Livro com o ISBN %s não foi encontrado.", isbn)));
        return ResponseEntity.ok(LivroResponse.from(livro));
    }
}
