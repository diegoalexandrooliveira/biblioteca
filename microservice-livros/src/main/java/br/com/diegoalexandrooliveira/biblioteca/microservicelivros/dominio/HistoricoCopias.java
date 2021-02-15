package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class HistoricoCopias {

    @Id
    @GeneratedValue(generator = "historico_copia_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "historico_copia_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Livro livro;

    @Enumerated(EnumType.STRING)
    private Acao acao;

    private String usuario;

    private int quantidadeAposAcao;

    private ZonedDateTime dataHora;

    public HistoricoCopias(@NonNull Livro livro, @NonNull Acao acao, @NonNull String usuario) {
        this.livro = livro;
        this.acao = acao;
        this.usuario = usuario;
        this.quantidadeAposAcao = livro.getQuantidadeDeCopias();
        this.dataHora = ZonedDateTime.now();
    }
}
