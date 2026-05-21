package com.github.sangueamigo.modules.conta.event;

public record SenhaRecuperacaoSolicitadaEvent(
        String email,
        String resetToken
) {
}
