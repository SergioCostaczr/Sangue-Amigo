package com.github.sangueamigo.modules.conta.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenhaRequest(
        @NotBlank @Email String email
) {}
