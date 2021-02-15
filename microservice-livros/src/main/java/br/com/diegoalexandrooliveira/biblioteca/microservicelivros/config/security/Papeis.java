package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Papeis {
    public static final String ADMIN = "administrador";
    public static final String USUARIO = "usuario";
}
