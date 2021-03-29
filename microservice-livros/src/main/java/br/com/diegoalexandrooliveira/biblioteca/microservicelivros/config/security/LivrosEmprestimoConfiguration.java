package br.com.diegoalexandrooliveira.biblioteca.microservicelivros.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

@Configuration
@RequiredArgsConstructor
@SuppressWarnings("java:S1874")
public class LivrosEmprestimoConfiguration {

    private final EmprestimoOAuth2Properties emprestimoOAuth2Properties;

    @Bean
    public OAuth2FeignRequestInterceptor requestInterceptor() {
        OAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
        ResourceOwnerPasswordResourceDetails resourceOwnerPasswordResourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceOwnerPasswordResourceDetails.setUsername(emprestimoOAuth2Properties.getUsername());
        resourceOwnerPasswordResourceDetails.setPassword(emprestimoOAuth2Properties.getPassword());
        resourceOwnerPasswordResourceDetails.setAccessTokenUri(emprestimoOAuth2Properties.getAccessTokenUri());
        resourceOwnerPasswordResourceDetails.setClientId(emprestimoOAuth2Properties.getClientId());
        return new OAuth2FeignRequestInterceptor(clientContext, resourceOwnerPasswordResourceDetails);
    }
}
