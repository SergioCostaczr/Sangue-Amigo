package com.github.sangueamigo.modules.doacao.service;

import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import com.github.sangueamigo.modules.doacao.dto.DoacaoResponse;
import com.github.sangueamigo.modules.doacao.entity.Doacao;
import com.github.sangueamigo.modules.doacao.repository.DoacaoRepository;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.exception.HemocentroNaoEncontradoException;
import com.github.sangueamigo.modules.hemocentro.repository.HemocentroRepository;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.exception.UsuarioNaoEncontradoException;
import com.github.sangueamigo.modules.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HemocentroRepository hemocentroRepository;

    public boolean jaRegistrada(Long agendamentoId){
        return doacaoRepository.existsByAgendamentoId(agendamentoId);
    }

    @Transactional
    public Doacao registrar(Agendamento agendamento) {
        Doacao doacao = new Doacao();
        doacao.setAgendamento(agendamento);
        doacao.setDataDoacao(LocalDate.now());
        return doacaoRepository.save(doacao);
    }

    // DoacaoService
    public List<DoacaoResponse> listarHistoricoDoUsuario(Long contaId) {
        Usuario usuario = usuarioRepository.findByContaId(contaId)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        return doacaoRepository
                .findByAgendamentoUsuarioIdOrderByDataDoacaoDesc(usuario.getId())
                .stream()
                .map(DoacaoResponse::from)
                .toList();
    }

    public List<DoacaoResponse> listarHistoricoDoHemocentro(Long contaId) {
        Hemocentro hemocentro = hemocentroRepository.findByContaId(contaId)
                .orElseThrow(HemocentroNaoEncontradoException::new);

        return doacaoRepository
                .findByAgendamentoHemocentroIdOrderByDataDoacaoDesc(hemocentro.getId())
                .stream()
                .map(DoacaoResponse::from)
                .toList();
    }
}

