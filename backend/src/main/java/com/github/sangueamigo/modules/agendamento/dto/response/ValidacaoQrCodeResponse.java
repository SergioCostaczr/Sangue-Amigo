package com.github.sangueamigo.modules.agendamento.dto.response;

import java.time.LocalDate;

public record ValidacaoQrCodeResponse(
        Long doacaoId,
        String nomeUsuario,
        String tipoSanguineo,
        LocalDate dataDoacao
) {}