package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Emprestimo {

    @Id
    private Long id;

    private Cliente pessoa;

    private Set<Livro> livros;

    private Situacao situacao;

    private Aprovador aprovador;

    private ZonedDateTime dataEmprestimo;

    private ZonedDateTime dataAcordadaDevolucao;

    private ZonedDateTime dataDevolucao;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Cliente pessoa;
        private Set<Livro> livros;
        private Aprovador aprovador;
        private ZonedDateTime dataAcordadaDevolucao;
        private ZonedDateTime dataDevolucao;

        private Builder() {
        }

        public Builder pessoa(@NonNull Cliente pessoa) {
            this.pessoa = pessoa;
            return this;
        }

        public Builder livros(@NonNull Set<Livro> livros) {
            if (livros.isEmpty()) {
                throw new IllegalArgumentException("Livros não pode ser vazio.");
            }
            this.livros = livros;
            return this;
        }

        public Builder aprovador(@NonNull Aprovador aprovador) {
            this.aprovador = aprovador;
            return this;
        }

        public Builder dataAcordadaDevolucao(@NonNull ZonedDateTime dataAcordadaDevolucao) {
            this.dataAcordadaDevolucao = dataAcordadaDevolucao;
            return this;
        }

        public Builder dataDevolucao(@NonNull ZonedDateTime dataDevolucao) {
            this.dataDevolucao = dataDevolucao;
            return this;
        }

        public Emprestimo build() {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.dataAcordadaDevolucao = this.dataAcordadaDevolucao;
            emprestimo.pessoa = this.pessoa;
            emprestimo.dataDevolucao = this.dataDevolucao;
            emprestimo.livros = this.livros;
            emprestimo.aprovador = this.aprovador;
            emprestimo.situacao = Situacao.ABERTO;
            emprestimo.dataEmprestimo = ZonedDateTime.now();
            emprestimo.id = ThreadLocalRandom.current().nextLong(1, 1_000_000_001);
            return emprestimo;
        }
    }
}
