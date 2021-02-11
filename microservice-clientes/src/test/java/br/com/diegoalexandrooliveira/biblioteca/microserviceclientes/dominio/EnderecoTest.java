package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnderecoTest {

    @DisplayName("Deve criar um endereço")
    @Test
    void teste1(){
        Endereco endereco = Endereco.builder()
                .logradouro("Cidade")
                .numero(1)
                .cidade("Cidade")
                .estado("Estado")
                .build();

        assertNotNull(endereco);
    }

    @DisplayName("Não deve criar um endereço sem logradouro")
    @Test
    void teste2(){
        Endereco.EnderecoBuilder enderecoBuilder = Endereco.builder()
                .numero(1)
                .cidade("Cidade")
                .estado("Estado");

        assertThrows(IllegalArgumentException.class, enderecoBuilder::build);
    }

    @DisplayName("Não deve criar um endereço sem cidade")
    @Test
    void teste3(){
        Endereco.EnderecoBuilder enderecoBuilder = Endereco.builder()
                .logradouro("Cidade")
                .numero(1)
                .estado("Estado");

        assertThrows(IllegalArgumentException.class, enderecoBuilder::build);
    }

    @DisplayName("Não deve criar um endereço sem estado")
    @Test
    void teste4(){
        Endereco.EnderecoBuilder enderecoBuilder = Endereco.builder()
                .logradouro("Cidade")
                .numero(1)
                .cidade("Cidade");

        assertThrows(IllegalArgumentException.class, enderecoBuilder::build);
    }

}