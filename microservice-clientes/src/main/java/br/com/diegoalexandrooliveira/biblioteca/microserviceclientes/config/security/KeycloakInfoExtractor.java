package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.config.security;

import java.util.Set;

public interface KeycloakInfoExtractor {
    String getUsuario();

    Set<String> getPapeis();
}
