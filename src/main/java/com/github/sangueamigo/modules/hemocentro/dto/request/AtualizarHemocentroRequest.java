package com.github.sangueamigo.modules.hemocentro.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarHemocentroRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank String endereco,
        String cidade,
        String estado
) {
}
