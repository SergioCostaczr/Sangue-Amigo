package com.github.sangueamigo.modules.agendamento.dto;

import jakarta.validation.constraints.NotNull;

public record CriarAgendamentoRequest(
        @NotNull Long horarioId
) {}
