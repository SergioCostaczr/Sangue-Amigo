package com.github.sangueamigo.modules.hemocentro.dto.response;

import com.github.sangueamigo.modules.horariodisponivel.entity.HorarioDisponivel;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioDisponivelResponse(
        Long id,
        LocalDate data,
        LocalTime hora,
        Integer vagas,
        Boolean disponivel,
        Long hemocentroId,
        String nomeHemocentro
) {
    public static HorarioDisponivelResponse from(HorarioDisponivel horario) {
        return new HorarioDisponivelResponse(
                horario.getId(),
                horario.getData(),
                horario.getHora(),
                horario.getVagas(),
                horario.getDisponivel(),
                horario.getHemocentro().getId(),
                horario.getHemocentro().getNome()
        );
    }
}
