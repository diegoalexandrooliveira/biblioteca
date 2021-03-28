package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Document
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Emprestimo {

    @Id
    private ObjectId id;

    private Cliente pessoa;

    private Set<Livro> livros;

    private Situacao situacao;

    private Aprovador aprovador;

    private String dataEmprestimo;

    private String dataAcordadaDevolucao;

    private String dataDevolucao;

    public static Builder builder() {
        return new Builder();
    }

    public String getNomePessoa() {
        return pessoa.getNomeCompleto();
    }

    public ZonedDateTime getDataAcordadaDevolucao() {
        return ZonedDateTime.parse(dataAcordadaDevolucao);
    }

    public ZonedDateTime getDataEmprestimo() {
        return ZonedDateTime.parse(dataEmprestimo);
    }

    public Optional<ZonedDateTime> getDataDevolucao() {
        return dataDevolucao == null || dataDevolucao.isEmpty() ?
                Optional.empty() :
                Optional.of(ZonedDateTime.parse(dataDevolucao));
    }

    public String getId() {
        return id.toHexString();
    }

    public static final class Builder {
        private Cliente pessoa;
        private Set<Livro> livros;
        private Aprovador aprovador;
        private ZonedDateTime dataAcordadaDevolucao;

        private Builder() {
        }

        public Builder pessoa(@NonNull Cliente pessoa) {
            this.pessoa = pessoa;
            if (!pessoa.isHabilitado()) {
                throw new IllegalStateException(String.format("Pessoa %s não pode realizar emprestimo porque esta desabilitada.", pessoa.getNomeCompleto()));
            }
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

        public Emprestimo build() {
            Emprestimo emprestimo = new Emprestimo();
            emprestimo.dataAcordadaDevolucao = this.dataAcordadaDevolucao.format(DateTimeFormatter.ISO_DATE_TIME);
            emprestimo.pessoa = this.pessoa;
            emprestimo.livros = this.livros;
            emprestimo.aprovador = this.aprovador;
            emprestimo.situacao = Situacao.ABERTO;
            emprestimo.dataEmprestimo = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("GMT")).format(DateTimeFormatter.ISO_DATE_TIME);
            emprestimo.id = new ObjectId();
            return emprestimo;
        }
    }
}
