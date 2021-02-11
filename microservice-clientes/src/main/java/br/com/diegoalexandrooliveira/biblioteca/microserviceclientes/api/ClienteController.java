package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


}
