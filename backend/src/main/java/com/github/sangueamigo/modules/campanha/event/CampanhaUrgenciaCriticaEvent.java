package com.github.sangueamigo.modules.campanha.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record CampanhaUrgenciaCriticaEvent(
        Long campanhaId,
        String titulo,
        String descricao,
        String nomeHemocentro,
        LocalDate dataInicio,
        LocalDate dataFim,
        String endereco,
        String cidade,
        String estado,
        Set<String> tiposSanguineosNecessarios,
        List<DestinatarioCampanhaCritica> destinatarios
) {
}
