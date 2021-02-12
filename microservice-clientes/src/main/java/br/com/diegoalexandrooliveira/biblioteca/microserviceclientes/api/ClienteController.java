package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@Validated
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<ClienteResponse> salvar(@RequestBody @Valid NovoClienteRequest novoClienteRequest) {
        Cliente cliente = clienteRepository.save(novoClienteRequest.converter());
        return ResponseEntity.ok(ClienteResponse.of(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> recuperarTodos() {
        return ResponseEntity.ok(
                clienteRepository.findAll()
                        .stream()
                        .map(ClienteResponse::of)
                        .collect(Collectors.toList()));
    }

    @PutMapping(value = "/inativa/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ClienteResponse> inativar(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("Id %s não encontrado", id)));
        cliente.inativar();
        clienteRepository.save(cliente);
        return ResponseEntity.ok(ClienteResponse.of(cliente));
    }


}
