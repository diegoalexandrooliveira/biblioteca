package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ClienteResponse {

    private Long id;

    private String usuario;

    private String nomeCompleto;

    private String cpf;

    private String logradouro;

    private int numero;

    private String cidade;

    private String estado;


    public static ClienteResponse of(Cliente cliente) {
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.id = cliente.getId();
        clienteResponse.usuario = cliente.getUsuario();
        clienteResponse.nomeCompleto = cliente.getNomeCompleto();
        clienteResponse.cpf = cliente.getCpf();
        clienteResponse.logradouro = cliente.getLogradouro();
        clienteResponse.numero = cliente.getNumero();
        clienteResponse.cidade = cliente.getCidade();
        clienteResponse.estado = cliente.getEstado();
        return clienteResponse;
    }
}
