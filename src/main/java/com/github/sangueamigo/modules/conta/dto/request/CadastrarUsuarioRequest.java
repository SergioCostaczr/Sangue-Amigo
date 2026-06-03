package com.github.sangueamigo.modules.conta.dto.request;

import com.github.sangueamigo.modules.usuario.enums.Sexo;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CadastrarUsuarioRequest(
        @NotBlank   String nome,
        @NotBlank  @CPF String cpf,
        @NotBlank   @Email String email,
        @NotBlank   String senha,
        @NotNull    LocalDate dataNascimento,
        @NotNull    TipoSanguineo tipoSanguineo,
        @NotNull    Sexo sexo,
                    String telefone
) {
}
