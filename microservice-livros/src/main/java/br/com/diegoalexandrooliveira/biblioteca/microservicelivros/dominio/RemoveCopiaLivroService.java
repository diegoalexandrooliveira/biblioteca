package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.dominio;

import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api.EnviarRemoveCopiaLivro;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api.LivroEmprestimoClient;
import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.api.LivroEmprestimoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveCopiaLivroService {

    private final EnviarRemoveCopiaLivro enviarRemoveCopiaLivro;
    private final LivroRepository livroRepository;
    private final LivroEmprestimoClient livroEmprestimoClient;

    public void removeCopia(Livro livro) {
        livro.removeCopia();
        LivroEmprestimoResponse livroEmprestimoResponse = livroEmprestimoClient.procuraPorIsbn(livro.getIsbn());
        if (livroEmprestimoResponse.getQuantidadeCopiasDisponiveis() == 0) {
            throw new IllegalStateException("Não há cópias dísponíveis para serem removidas, aguarde a devolução dos emprestimos abertos.");
        }
        livroRepository.save(livro);
        enviarRemoveCopiaLivro.enviar(livro);
    }
}
