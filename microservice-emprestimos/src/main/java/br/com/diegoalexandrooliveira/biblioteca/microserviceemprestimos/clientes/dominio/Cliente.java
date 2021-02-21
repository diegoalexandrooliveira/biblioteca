package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.ThreadLocalRandom;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cliente {

    @Id
    private Long id;

    private String usuario;

    private String nomeCompleto;

    private boolean habilitado;

    private int quantidadeEmprestimos;

    public Cliente(String usuario, String nomeCompleto, boolean habilitado) {
        this.usuario = usuario;
        this.nomeCompleto = nomeCompleto;
        this.habilitado = habilitado;
        this.quantidadeEmprestimos = 0;
        this.id = ThreadLocalRandom.current().nextLong(1_000_000);
    }

    public void alteraDadosPessoais(String nomeCompleto, boolean habilitado) {
        this.nomeCompleto = nomeCompleto;
        this.habilitado = habilitado;
    }
}
