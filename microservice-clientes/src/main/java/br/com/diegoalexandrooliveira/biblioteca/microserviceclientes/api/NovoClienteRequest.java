package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Endereco;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class NovoClienteRequest {

    @NotBlank
    private String usuario;

    @NotBlank
    private String nomeCompleto;

    @CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String logradouro;

    @NotNull
    private int numero;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    Cliente converter() {
        Endereco endereco = Endereco.builder()
                .logradouro(logradouro)
                .numero(numero)
                .cidade(cidade)
                .estado(estado)
                .build();
        return new Cliente(usuario, nomeCompleto, cpf, endereco);
    }
}
