package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;


import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.config.security.KeycloakInfoExtractor;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Emprestimo;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.EmprestimoRepository;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.NovoEmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

import static br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.config.security.Papeis.ADMIN;

@RestController
@RequestMapping(path = "/emprestimos")
@Validated
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final KeycloakInfoExtractor keycloakInfoExtractor;
    private final NovoEmprestimoService novoEmprestimoService;

    @GetMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<List<Emprestimo>> recuperarTodos() {
        return ResponseEntity.ok(emprestimoRepository.findAll());
    }

    @PostMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<NovoEmprestimoResponse> realizarEmprestimo(@RequestBody @Valid NovoEmprestimoRequest novoEmprestimoRequest) {
        String usuarioAprovador = keycloakInfoExtractor.getUsuario();
        Emprestimo emprestimo = novoEmprestimoService.efetivarEmprestimo(novoEmprestimoRequest, usuarioAprovador);
        return ResponseEntity.ok(NovoEmprestimoResponse.from(emprestimo));
    }
}
