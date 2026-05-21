package com.github.sangueamigo.modules.hemocentro.dto.response;

import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;

public record HemocentroResponse(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco,
        String cidade,
        String estado
) {
    public static HemocentroResponse from(Hemocentro hemocentro) {
        return new HemocentroResponse(
                hemocentro.getId(),
                hemocentro.getNome(),
                hemocentro.getCnpj(),
                hemocentro.getConta().getEmail(),
                hemocentro.getTelefone(),
                hemocentro.getEndereco(),
                hemocentro.getCidade(),
                hemocentro.getEstado()
        );
    }
}
