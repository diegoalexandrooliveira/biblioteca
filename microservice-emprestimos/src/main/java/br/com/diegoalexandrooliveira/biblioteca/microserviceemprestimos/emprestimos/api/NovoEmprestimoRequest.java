package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NovoEmprestimoRequest {

    @NotNull
    private String usuarioRequerente;

    @Size(min = 1)
    @NotNull
    private Set<String> isbnLivros;

    @NotNull
    private ZonedDateTime dataParaDevolucao;

    @JsonSetter
    public void setDataParaDevolucao(String dataParaDevolucao) {
        this.dataParaDevolucao = ZonedDateTime.parse(dataParaDevolucao, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(ZoneId.of("GMT"));
    }
}
