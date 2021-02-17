package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.comum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER
})
@Constraint(validatedBy = UnicoValorValidador.class)
public @interface UnicoValor {

    String entidade();

    String campoUnico();

    String campoId() default "id";

    String message() default "duplicado.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
