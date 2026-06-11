package com.github.sangueamigo.modules.campanha.dto.request;

import com.github.sangueamigo.modules.campanha.enums.StatusCampanha;
import com.github.sangueamigo.modules.campanha.enums.UrgenciaCampanha;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record AtualizarCampanhaRequest(
        @NotBlank String titulo,
        @Size(max = 500) String descricao,
        String urlImagem,
        @NotEmpty Set<TipoSanguineo> tiposSanguineosNecessarios,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        String endereco,
        String cidade,
        String estado,
        @NotNull StatusCampanha status,
        @NotNull UrgenciaCampanha urgencia
) {
}
