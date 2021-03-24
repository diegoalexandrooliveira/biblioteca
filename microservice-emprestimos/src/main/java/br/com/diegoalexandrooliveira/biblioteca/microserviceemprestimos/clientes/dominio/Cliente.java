package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cliente {

    @Id
    private ObjectId id;

    private String usuario;

    private String nomeCompleto;

    private boolean habilitado;

    private int quantidadeEmprestimos;

    public Cliente(String usuario, String nomeCompleto, boolean habilitado) {
        this.usuario = usuario;
        this.nomeCompleto = nomeCompleto;
        this.habilitado = habilitado;
        this.quantidadeEmprestimos = 0;
        this.id = new ObjectId();
    }

    public void alteraDadosPessoais(String nomeCompleto, boolean habilitado) {
        this.nomeCompleto = nomeCompleto;
        this.habilitado = habilitado;
    }
}
