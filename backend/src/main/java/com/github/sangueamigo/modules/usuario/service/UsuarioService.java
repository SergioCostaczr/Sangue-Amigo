package com.github.sangueamigo.modules.usuario.service;

import com.github.sangueamigo.modules.usuario.dto.request.AtualizarUsuarioRequest;
import com.github.sangueamigo.modules.usuario.dto.response.UsuarioNotificacaoResponse;
import com.github.sangueamigo.modules.usuario.dto.response.UsuarioResponse;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import com.github.sangueamigo.modules.usuario.exception.UsuarioNaoEncontradoException;
import com.github.sangueamigo.modules.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse buscarPerfil(Long contaId) {
        return UsuarioResponse.from(buscarUsuarioPorContaId(contaId));
    }

    @Transactional
    public UsuarioResponse atualizarPerfil(Long contaId, AtualizarUsuarioRequest request) {
        Usuario usuario = buscarUsuarioPorContaId(contaId);

        usuario.setNome(request.nome());
        usuario.setTelefone(request.telefone());
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setTipoSanguineo(request.tipoSanguineo());
        usuario.setSexo(request.sexo());

        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    public List<UsuarioNotificacaoResponse> listarDestinatariosCompativeis(Collection<TipoSanguineo> tipos) {
        if (tipos == null || tipos.isEmpty()) {
            return List.of();
        }

        return usuarioRepository.findByTipoSanguineoIn(new ArrayList<>(tipos))
                .stream()
                .map(UsuarioNotificacaoResponse::from)
                .toList();
    }

    private Usuario buscarUsuarioPorContaId(Long contaId) {
        return usuarioRepository.findByContaId(contaId)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
