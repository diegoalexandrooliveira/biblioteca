package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;


import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security.KeycloakInfoExtractor;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.HistoricoCopias;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.HistoricoCopiasRepository;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security.Papeis.ADMIN;
import static br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Acao.ADICIONA;
import static br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.Acao.REMOVE;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Validated
public class LivroController {

    private final LivroRepository livroRepository;
    private final HistoricoCopiasRepository historicoCopiasRepository;
    private final KeycloakInfoExtractor keycloakInfoExtractor;


    @PostMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<LivroResponse> salvar(@RequestBody @Valid NovoLivroRequest novoLivroRequest) {
        Livro livro = novoLivroRequest.converte();
        livroRepository.save(livro);
        return ResponseEntity.ok(LivroResponse.of(livro));
    }

    @GetMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<List<LivroResponse>> recuperaTodos() {
        return ResponseEntity
                .ok(livroRepository
                        .findAll()
                        .stream()
                        .map(LivroResponse::of)
                        .collect(Collectors.toList()));
    }

    @PutMapping("/{id}/adicionaCopia")
    @RolesAllowed(ADMIN)
    public ResponseEntity<Integer> adicionaCopia(@PathVariable("id") Long id) {
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        livro.adicionaCopia();
        HistoricoCopias historicoCopias = new HistoricoCopias(livro, ADICIONA, keycloakInfoExtractor.getUsuario());
        livroRepository.save(livro);
        historicoCopiasRepository.save(historicoCopias);
        return ResponseEntity.ok(livro.getQuantidadeDeCopias());
    }

    @PutMapping("/{id}/removeCopia")
    @RolesAllowed(ADMIN)
    public ResponseEntity<Integer> removeCopia(@PathVariable("id") Long id) {
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        livro.removeCopia();
        HistoricoCopias historicoCopias = new HistoricoCopias(livro, REMOVE, keycloakInfoExtractor.getUsuario());
        livroRepository.save(livro);
        historicoCopiasRepository.save(historicoCopias);
        return ResponseEntity.ok(livro.getQuantidadeDeCopias());
    }


}
