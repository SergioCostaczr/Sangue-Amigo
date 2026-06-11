package com.github.sangueamigo.modules.agendamento.dto.request;

import jakarta.validation.constraints.NotNull;

public record CriarAgendamentoRequest(
        @NotNull Long horarioId
) {}
