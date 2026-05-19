package com.github.sangueamigo.modules.doacao.service;

import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import com.github.sangueamigo.modules.doacao.entity.Doacao;
import com.github.sangueamigo.modules.doacao.repository.DoacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;

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
}

