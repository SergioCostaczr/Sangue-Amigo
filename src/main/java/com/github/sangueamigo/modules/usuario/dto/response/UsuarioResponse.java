package com.github.sangueamigo.modules.usuario.dto.response;

import com.github.sangueamigo.modules.usuario.entity.Usuario;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        String tipoSanguineo,
        String sexo
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getConta().getEmail(),
                usuario.getTelefone(),
                usuario.getDataNascimento(),
                usuario.getTipoSanguineo().name(),
                usuario.getSexo().name()
        );
    }
}
