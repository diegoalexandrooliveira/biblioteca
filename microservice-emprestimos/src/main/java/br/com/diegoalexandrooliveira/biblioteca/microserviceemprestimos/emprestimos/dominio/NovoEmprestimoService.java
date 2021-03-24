package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.dominio.ClienteRepository;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api.NovoEmprestimoRequest;
import br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.livros.dominio.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NovoEmprestimoService {

    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;


    public void efetivarEmprestimo(NovoEmprestimoRequest novoEmprestimoRequest, String usuarioAprovador) {
        Aprovador aprovador = clienteRepository.procuraPorUsuario(usuarioAprovador)
                .map(cliente -> new Aprovador(cliente.getUsuario(), cliente.getNomeCompleto()))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Usuário %s não encontrado", usuarioAprovador)));


        Cliente cliente = clienteRepository.procuraPorUsuario(novoEmprestimoRequest.getUsuarioRequerente())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cliente %s não encontrado.", novoEmprestimoRequest.getUsuarioRequerente())));

        Emprestimo
                .builder()
                .aprovador(aprovador)
                .pessoa(cliente);


    }
}
