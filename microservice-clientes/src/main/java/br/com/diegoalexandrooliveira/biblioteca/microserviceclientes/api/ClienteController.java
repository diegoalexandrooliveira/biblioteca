package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.config.security.KeycloakInfoExtractor;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.ClienteRepository;
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

import static br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.config.security.Papeis.ADMIN;
import static br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.config.security.Papeis.USUARIO;

@RestController
@RequestMapping("/clientes")
@Validated
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final KeycloakInfoExtractor keycloakInfoExtractor;

    @PostMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<ClienteResponse> salvar(@RequestBody @Valid NovoClienteRequest novoClienteRequest) {
        Cliente cliente = clienteRepository.save(novoClienteRequest.converter());
        return ResponseEntity.ok(ClienteResponse.of(cliente));
    }

    @GetMapping
    @RolesAllowed(ADMIN)
    public ResponseEntity<List<ClienteResponse>> recuperarTodos() {
        return ResponseEntity.ok(
                clienteRepository.findAll()
                        .stream()
                        .map(ClienteResponse::of)
                        .collect(Collectors.toList()));
    }

    @GetMapping(value = "/{id}")
    @RolesAllowed({ADMIN, USUARIO})
    public ResponseEntity<ClienteResponse> recuperaPorId(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Id %s não encontrado", id)));
        if (!temAcessoAoUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Usuário %s não pode visualizar outro usuário", keycloakInfoExtractor.getUsuario()));
        }
        return ResponseEntity.ok(ClienteResponse.of(cliente));
    }

    @PutMapping(value = "/inativa/{id}", produces = "application/json;charset=UTF-8")
    @RolesAllowed({ADMIN, USUARIO})
    public ResponseEntity<ClienteResponse> inativar(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Id %s não encontrado", id)));
        if (!temAcessoAoUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Usuário %s não pode inativar outro usuário", keycloakInfoExtractor.getUsuario()));
        }
        cliente.inativar();
        clienteRepository.save(cliente);
        return ResponseEntity.ok(ClienteResponse.of(cliente));
    }

    private boolean temAcessoAoUsuario(String usuario) {
        if (keycloakInfoExtractor.getPapeis().contains(ADMIN)) {
            return true;
        }
        return keycloakInfoExtractor.getUsuario().equals(usuario);
    }


}
