package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.emprestimos.api;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DevolucaoRequest {

    @NotEmpty
    private String idEmprestimo;

    @NotNull
    private ZonedDateTime dataDevolucao;

    @JsonSetter
    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = ZonedDateTime.parse(dataDevolucao, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(ZoneId.of("GMT"));
    }
}
