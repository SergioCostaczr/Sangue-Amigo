package com.github.sangueamigo.modules.conta.event;

public record UsuarioCadastradoEvent(
        String email,
        String nome
) {
}
