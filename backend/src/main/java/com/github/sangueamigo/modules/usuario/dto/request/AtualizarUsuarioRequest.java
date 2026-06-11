package com.github.sangueamigo.modules.usuario.dto.request;

import com.github.sangueamigo.modules.usuario.enums.Sexo;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AtualizarUsuarioRequest(
        @NotBlank String nome,
        String telefone,
        @NotNull LocalDate dataNascimento,
        @NotNull TipoSanguineo tipoSanguineo,
        @NotNull Sexo sexo
) {
}
