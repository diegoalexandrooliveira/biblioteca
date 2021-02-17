package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.config.security;

import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
public class KeycloakInfoExtractorImpl implements KeycloakInfoExtractor {

    @Value("${keycloak.resource}")
    private String clientId;

    public String getUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return "";
        }
        return ((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    public Set<String> getPapeis() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return Set.of();
        }
        return ((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext().getToken().getResourceAccess(clientId).getRoles();
    }
}
