package com.github.sangueamigo.modules.agendamento.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ValidarQrCodeRequest(
        @NotBlank String qrCodeToken
) {}
