package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Emprestimo;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio.Situacao;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmprestimoResponse {

    private String id;

    private String nomeCliente;

    private Situacao situacaoEmprestimo;

    private ZonedDateTime dataEmprestimo;

    private ZonedDateTime dataParaDevolucao;

    private ZonedDateTime dataDevolucao;

    private Set<String> livros;

    public static EmprestimoResponse from(Emprestimo emprestimo) {
        EmprestimoResponse emprestimoResponse = new EmprestimoResponse();
        emprestimoResponse.id = emprestimo.getId();
        emprestimoResponse.nomeCliente = emprestimo.getNomePessoa();
        emprestimoResponse.dataEmprestimo = emprestimo.getDataEmprestimo();
        emprestimoResponse.dataParaDevolucao = emprestimo.getDataAcordadaDevolucao();
        emprestimoResponse.situacaoEmprestimo = emprestimo.getSituacao();
        emprestimoResponse.dataDevolucao = emprestimo.getDataDevolucao().orElse(null);
        emprestimoResponse.livros = emprestimo.getLivros().stream().map(Livro::getTitulo).collect(Collectors.toSet());
        return emprestimoResponse;
    }
}
