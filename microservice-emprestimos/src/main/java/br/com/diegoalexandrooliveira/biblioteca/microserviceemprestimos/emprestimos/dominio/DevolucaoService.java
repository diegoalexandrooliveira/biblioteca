package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api.DevolucaoRequest;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevolucaoService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;


    public Emprestimo devolver(@NonNull DevolucaoRequest devolucaoRequest) {

        Emprestimo emprestimo = emprestimoRepository.findById(new ObjectId(devolucaoRequest.getIdEmprestimo()))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Emprestimo com identificador %s não encotnrado", devolucaoRequest.getIdEmprestimo())));
        emprestimo.encerrar(devolucaoRequest.getDataDevolucao());

        Set<Livro> livros = emprestimo
                .getLivros()
                .stream()
                .map(livro -> livroRepository.findByIsbn(livro.getIsbn()).orElseThrow(() -> new IllegalStateException(String.format("Livro com ISBN %s não encontrado.", livro.getIsbn()))))
                .collect(Collectors.toSet());

        Emprestimo emprestimoEncerrado = emprestimoRepository.save(emprestimo);

        livros.forEach(livro -> {
            livro.devolverCopia();
            livroRepository.save(livro);
        });

        return emprestimoEncerrado;
    }
}
