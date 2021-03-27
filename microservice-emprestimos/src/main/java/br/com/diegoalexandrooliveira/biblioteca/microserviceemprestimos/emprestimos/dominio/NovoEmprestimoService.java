package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api.NovoEmprestimoRequest;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.Livro;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovoEmprestimoService {

    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;


    public Emprestimo efetivarEmprestimo(NovoEmprestimoRequest novoEmprestimoRequest, String usuarioAprovador) {
        Aprovador aprovador = clienteRepository.procuraPorUsuario(usuarioAprovador)
                .map(cliente -> new Aprovador(cliente.getUsuario(), cliente.getNomeCompleto()))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Usuário %s não encontrado", usuarioAprovador)));


        Cliente cliente = clienteRepository.procuraPorUsuario(novoEmprestimoRequest.getUsuarioRequerente())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cliente %s não encontrado.", novoEmprestimoRequest.getUsuarioRequerente())));

        Set<Livro> livros = novoEmprestimoRequest
                .getIsbnLivros()
                .stream()
                .map(isbn -> livroRepository.findByIsbn(isbn).orElseThrow(() -> new IllegalArgumentException(String.format("ISBN %s não encontrado.", isbn))))
                .collect(Collectors.toSet());

        livros.forEach(Livro::realizaEmprestimo);

        Emprestimo emprestimo = Emprestimo
                .builder()
                .aprovador(aprovador)
                .pessoa(cliente)
                .livros(livros)
                .dataAcordadaDevolucao(novoEmprestimoRequest.getDataParaDevolucao())
                .build();
        emprestimoRepository.save(emprestimo);

        livros.forEach(livroRepository::save);

        return emprestimo;
    }
}
