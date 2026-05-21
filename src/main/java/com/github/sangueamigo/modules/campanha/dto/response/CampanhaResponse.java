package com.github.sangueamigo.modules.campanha.dto.response;

import com.github.sangueamigo.modules.campanha.entity.Campanha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record CampanhaResponse(
        Long id,
        String titulo,
        String descricao,
        String urlImagem,
        Set<String> tiposSanguineosNecessarios,
        LocalDate dataInicio,
        LocalDate dataFim,
        String endereco,
        String cidade,
        String estado,
        String status,
        String urgencia,
        Long hemocentroId,
        String nomeHemocentro,
        LocalDateTime criadaEm
) {
    public static CampanhaResponse from(Campanha campanha) {
        return new CampanhaResponse(
                campanha.getId(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getUrlImagem(),
                campanha.getTiposSanguineosNecessarios()
                        .stream()
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toSet()),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.getEndereco(),
                campanha.getCidade(),
                campanha.getEstado(),
                campanha.getStatus().name(),
                campanha.getUrgencia().name(),
                campanha.getHemocentro().getId(),
                campanha.getHemocentro().getNome(),
                campanha.getCriadaEm()
        );
    }
}
