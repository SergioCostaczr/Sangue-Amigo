package com.github.sangueamigo.modules.hemocentro.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CriarHorarioDisponivelRequest(
        @NotNull LocalDate data,
        @NotNull LocalTime hora,
        @NotNull @Min(1) Integer vagas
) {
}
