package com.github.sangueamigo.modules.agendamento.event;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoConfirmadoEvent(
        String email,
        String nomeUsuario,
        String nomeHemocentro,
        LocalDate data,
        LocalTime horario,
        String qrCodeToken
) {
}
