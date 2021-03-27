package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Emprestimo;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Situacao;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NovoEmprestimoResponse {

    private String id;

    private String nomeCliente;

    private Situacao situacaoEmprestimo;

    private ZonedDateTime dataParaDevolucao;

    public static NovoEmprestimoResponse from(Emprestimo emprestimo) {
        NovoEmprestimoResponse novoEmprestimoResponse = new NovoEmprestimoResponse();
        novoEmprestimoResponse.id = emprestimo.getId();
        novoEmprestimoResponse.nomeCliente = emprestimo.getNomePessoa();
        novoEmprestimoResponse.dataParaDevolucao = emprestimo.getDataAcordadaDevolucao();
        novoEmprestimoResponse.situacaoEmprestimo = emprestimo.getSituacao();
        return novoEmprestimoResponse;
    }
}
