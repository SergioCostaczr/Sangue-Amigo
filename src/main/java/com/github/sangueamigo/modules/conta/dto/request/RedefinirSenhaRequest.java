package com.github.sangueamigo.modules.conta.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequest(
        @NotBlank String token,
        @NotBlank String novaSenha
) {
}
