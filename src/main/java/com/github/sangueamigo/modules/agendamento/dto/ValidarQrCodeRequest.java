package com.github.sangueamigo.modules.agendamento.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidarQrCodeRequest(
        @NotBlank String qrCodeToken
) {}
