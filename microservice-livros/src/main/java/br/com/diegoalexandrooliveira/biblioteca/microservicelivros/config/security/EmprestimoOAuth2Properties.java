package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "emprestimo.oauth2")
@Configuration
@Getter
@Setter
public class EmprestimoOAuth2Properties {
    private String username;
    private String password;
    private String accessTokenUri;
    private String clientId;
}
