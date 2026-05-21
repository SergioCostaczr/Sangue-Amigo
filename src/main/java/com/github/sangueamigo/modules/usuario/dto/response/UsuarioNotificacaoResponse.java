package com.github.sangueamigo.modules.usuario.dto.response;

import com.github.sangueamigo.modules.usuario.entity.Usuario;

public record UsuarioNotificacaoResponse(
        String email,
        String nome,
        String tipoSanguineo
) {
    public static UsuarioNotificacaoResponse from(Usuario usuario) {
        return new UsuarioNotificacaoResponse(
                usuario.getConta().getEmail(),
                usuario.getNome(),
                usuario.getTipoSanguineo().name()
        );
    }
}
