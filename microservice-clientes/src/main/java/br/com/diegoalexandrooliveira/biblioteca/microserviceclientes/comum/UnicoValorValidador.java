package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.comum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UnicoValorValidador implements ConstraintValidator<UnicoValor, Object> {

    private final EntityManager entityManager;
    private final HttpServletRequest servletRequest;

    private String entidade;
    private String campoUnico;
    private String campoId;


    @Override
    public void initialize(UnicoValor anotacao) {
        this.entidade = anotacao.entidade();
        this.campoUnico = anotacao.campoUnico();
        this.campoId = anotacao.campoId();
    }

    @Override
    public boolean isValid(Object valor, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(valor)) {
            return true;
        }

        Optional<Long> idDaRequisicao = recuperaIdDaRequisicao();

        TypedQuery<Long> query = idDaRequisicao
                .map(id -> {
                    String sql = String.format("select count(*) from %s where %s = ?1 and %s != ?2", entidade, campoUnico, campoId);
                    TypedQuery<Long> longTypedQuery = entityManager.createQuery(sql, Long.class);
                    longTypedQuery.setParameter(2, id);
                    return longTypedQuery;
                }).orElse(entityManager.createQuery(String.format("select count(*) from %s where %s = ?1", entidade, campoUnico), Long.class));

        query.setParameter(1, valor);
        return query.getSingleResult() == 0L;
    }

    private Optional<Long> recuperaIdDaRequisicao() {
        Map<?, ?> pathVariables = Optional.ofNullable(servletRequest.getAttribute("org.springframework.web.servlet.View.pathVariables"))
                .map(variables -> (Map<?, ?>) variables)
                .orElse(Collections.emptyMap());
        return Optional.ofNullable(pathVariables.get("id")).map(id -> Long.valueOf(id.toString()));
    }
}
