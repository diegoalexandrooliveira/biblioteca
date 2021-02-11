package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Cliente {

    @Id
    @GeneratedValue(generator = "cliente_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "cliente_sequence", sequenceName = "cliente_sequence", allocationSize = 1)
    @Getter
    private Long id;

    @Getter
    private String usuario;

    @Getter
    private String nomeCompleto;

    @Getter
    private String cpf;

    @Embedded
    private Endereco endereco;

    public Cliente(@NonNull String usuario, @NonNull String nomeCompleto, @NonNull String cpf, @NonNull Endereco endereco) {
        this.usuario = usuario;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    public String getLogradouro() {
        return endereco.getLogradouro();
    }

    public int getNumero() {
        return endereco.getNumero();
    }

    public String getCidade() {
        return endereco.getCidade();
    }

    public String getEstado() {
        return endereco.getEstado();
    }

}
