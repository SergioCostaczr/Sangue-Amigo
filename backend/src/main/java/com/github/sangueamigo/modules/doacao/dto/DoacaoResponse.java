package com.github.sangueamigo.modules.doacao.dto;

import com.github.sangueamigo.modules.doacao.entity.Doacao;

import java.time.LocalDate;

public record DoacaoResponse(
        Long id,
        LocalDate dataDoacao,
        String observacoes,
        Long agendamentoId,
        String nomeUsuario,
        String nomeHemocentro
) {
    public static DoacaoResponse from(Doacao d) {
        return new DoacaoResponse(
                d.getId(),
                d.getDataDoacao(),
                d.getObservacoes(),
                d.getAgendamento().getId(),
                d.getAgendamento().getUsuario().getNome(),
                d.getAgendamento().getHemocentro().getNome()
        );
    }
}