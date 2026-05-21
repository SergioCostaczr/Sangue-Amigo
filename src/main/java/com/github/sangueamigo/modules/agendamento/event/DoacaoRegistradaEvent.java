package com.github.sangueamigo.modules.agendamento.event;

import java.time.LocalDate;

public record DoacaoRegistradaEvent(
        String email,
        String nomeUsuario,
        String nomeHemocentro,
        LocalDate dataDoacao
) {
}
