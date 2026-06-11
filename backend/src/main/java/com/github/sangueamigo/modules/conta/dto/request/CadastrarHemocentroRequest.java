package com.github.sangueamigo.modules.conta.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CadastrarHemocentroRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank @CNPJ String cnpj,
        @NotBlank String senha,
        @NotBlank String telefone,
        @NotBlank String endereco,
        String cidade,
        String estado
) {
}
