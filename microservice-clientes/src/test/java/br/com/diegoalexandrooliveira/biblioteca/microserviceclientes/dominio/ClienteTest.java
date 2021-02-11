package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {


    @DisplayName("Deve criar um cliente")
    @Test
    void teste1() {
        Endereco endereco = Endereco.builder()
                .logradouro("R 1")
                .numero(1)
                .cidade("Cidade")
                .estado("Estado")
                .build();

        Cliente cliente = new Cliente("usuario", "Nome do cliente", "11111111", endereco);

        assertNotNull(cliente);
        assertEquals("usuario", cliente.getUsuario());
        assertEquals("Nome do cliente", cliente.getNomeCompleto());
        assertEquals("11111111", cliente.getCpf());
        assertEquals("R 1", cliente.getLogradouro());
        assertEquals(1, cliente.getNumero());
        assertEquals("Cidade", cliente.getCidade());
        assertEquals("Estado", cliente.getEstado());
    }

    @DisplayName("Não deve criar um cliente com endereço nulo")
    @Test
    void teste2() {
        assertThrows(NullPointerException.class, () -> new Cliente("usuario", "Nome do cliente", "11111111", null));
    }

    @DisplayName("Não deve criar um cliente sem usuario")
    @Test
    void teste3() {
        Endereco endereco = Endereco.builder()
                .logradouro("R 1")
                .numero(1)
                .cidade("Cidade")
                .estado("Estado")
                .build();
        assertThrows(NullPointerException.class, () -> new Cliente(null, "Nome do cliente", "11111111", endereco));
    }

    @DisplayName("Não deve criar um cliente sem nomeCompleto")
    @Test
    void teste4() {
        Endereco endereco = Endereco.builder()
                .logradouro("R 1")
                .numero(1)
                .cidade("Cidade")
                .estado("Estado")
                .build();
        assertThrows(NullPointerException.class, () -> new Cliente("Usuario", null, "11111111", endereco));
    }

    @DisplayName("Não deve criar um cliente sem cpf")
    @Test
    void teste5() {
        Endereco endereco = Endereco.builder()
                .logradouro("R 1")
                .numero(1)
                .cidade("Cidade")
                .estado("Estado")
                .build();
        assertThrows(NullPointerException.class, () -> new Cliente("Usuario", "Nome do usuario", null, endereco));
    }

}