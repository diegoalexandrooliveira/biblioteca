package br.com.diegoalexandrooliveira.biblioteca.microservicelivros;

import br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security.KeycloakInfoExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class PapeisConfiguration {

    @Bean
    public KeycloakInfoExtractor keycloakInfoExtractor() {
        return new KeycloakInfoExtractor() {
            @Override
            public String getUsuario() {
                return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            }

            @Override
            public Set<String> getPapeis() {
                return SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .map(role -> role.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toSet());
            }
        };
    }
}
