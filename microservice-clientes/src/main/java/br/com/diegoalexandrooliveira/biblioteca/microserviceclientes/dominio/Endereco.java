package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter(AccessLevel.PACKAGE)
public class Endereco {

    private String logradouro;

    private int numero;

    private String cidade;

    private String estado;

    public static EnderecoBuilder builder() {
        return new EnderecoBuilder();
    }

    public static final class EnderecoBuilder {
        private String logradouro;
        private int numero;
        private String cidade;
        private String estado;

        private EnderecoBuilder() {
        }

        public EnderecoBuilder logradouro(String logradouro) {
            this.logradouro = logradouro;
            return this;
        }

        public EnderecoBuilder numero(int numero) {
            this.numero = numero;
            return this;
        }

        public EnderecoBuilder cidade(String cidade) {
            this.cidade = cidade;
            return this;
        }

        public EnderecoBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public Endereco build() {
            Assert.hasText(logradouro, "Logradouro deve ser preenchido.");
            Assert.hasText(cidade, "Cidade deve ser preenchido.");
            Assert.hasText(estado, "Estado deve ser preenchido.");
            Endereco endereco = new Endereco();
            endereco.logradouro = this.logradouro;
            endereco.cidade = this.cidade;
            endereco.numero = this.numero;
            endereco.estado = this.estado;
            return endereco;
        }
    }
}
