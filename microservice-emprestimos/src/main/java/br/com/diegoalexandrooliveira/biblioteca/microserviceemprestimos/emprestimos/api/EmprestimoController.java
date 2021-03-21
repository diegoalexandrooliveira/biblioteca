package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;


import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Emprestimo;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;

    @GetMapping
    public ResponseEntity<List<Emprestimo>> recuperarTodos() {
        return ResponseEntity.ok(emprestimoRepository.findAll());
    }
}
