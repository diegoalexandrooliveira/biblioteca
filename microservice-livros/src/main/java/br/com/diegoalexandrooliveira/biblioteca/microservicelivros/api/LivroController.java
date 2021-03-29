package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api;


import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security.KeycloakInfoExtractor;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio.*;
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
    private final EnviarNovoLivro enviarNovoLivro;
    private final EnviarNovaCopiaLivro enviarNovaCopiaLivro;
    private final RemoveCopiaLivroService removeCopiaLivroService;


    @PostMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<LivroResponse> salvar(@RequestBody @Valid NovoLivroRequest novoLivroRequest) {
        Livro livro = novoLivroRequest.converte();
        livroRepository.save(livro);
        enviarNovoLivro.enviar(livro);
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
        enviarNovaCopiaLivro.enviar(livro);
        return ResponseEntity.ok(livro.getQuantidadeDeCopias());
    }

    @PutMapping("/{id}/removeCopia")
    @RolesAllowed(ADMIN)
    public ResponseEntity<Integer> removeCopia(@PathVariable("id") Long id) {
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        removeCopiaLivroService.removeCopia(livro);
        HistoricoCopias historicoCopias = new HistoricoCopias(livro, REMOVE, keycloakInfoExtractor.getUsuario());
        historicoCopiasRepository.save(historicoCopias);
        return ResponseEntity.ok(livro.getQuantidadeDeCopias());
    }


}
