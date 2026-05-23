package com.github.sangueamigo.modules.hemocentro.service;

import com.github.sangueamigo.modules.agendamento.service.AgendamentoService;
import com.github.sangueamigo.modules.hemocentro.dto.request.AtualizarHemocentroRequest;
import com.github.sangueamigo.modules.hemocentro.dto.request.AtualizarHorarioDisponivelRequest;
import com.github.sangueamigo.modules.hemocentro.dto.request.CriarHorarioDisponivelRequest;
import com.github.sangueamigo.modules.hemocentro.dto.response.HemocentroResponse;
import com.github.sangueamigo.modules.hemocentro.dto.response.HorarioDisponivelResponse;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.exception.*;
import com.github.sangueamigo.modules.hemocentro.repository.HemocentroRepository;
import com.github.sangueamigo.modules.horariodisponivel.entity.HorarioDisponivel;
import com.github.sangueamigo.modules.horariodisponivel.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HemocentroService {

    private final HemocentroRepository hemocentroRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final AgendamentoService agendamentoService;

    public HemocentroResponse buscarPerfil(Long contaId) {
        return HemocentroResponse.from(buscarHemocentroPorContaId(contaId));
    }

    @Transactional
    public HemocentroResponse atualizarPerfil(Long contaId, AtualizarHemocentroRequest request) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);

        hemocentro.setNome(request.nome());
        hemocentro.setTelefone(request.telefone());
        hemocentro.setEndereco(request.endereco());
        hemocentro.setCidade(request.cidade());
        hemocentro.setEstado(request.estado());

        return HemocentroResponse.from(hemocentroRepository.save(hemocentro));
    }

    @Transactional
    public HorarioDisponivelResponse criarHorario(Long contaId, CriarHorarioDisponivelRequest request) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);

        HorarioDisponivel horario = new HorarioDisponivel();
        horario.setHemocentro(hemocentro);
        horario.setData(request.data());
        horario.setHora(request.hora());
        horario.setVagas(request.vagas());
        horario.setDisponivel(true);

        return salvarHorarioTratandoDuplicidade(horario);
    }

    @Transactional
    public HorarioDisponivelResponse atualizarHorario(
            Long contaId,
            Long horarioId,
            AtualizarHorarioDisponivelRequest request
    ) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        HorarioDisponivel horario = buscarHorarioDoHemocentro(horarioId, hemocentro.getId());

        horario.setData(request.data());
        horario.setHora(request.hora());
        horario.setVagas(request.vagas());
        horario.setDisponivel(request.disponivel());

        return salvarHorarioTratandoDuplicidade(horario);
    }

    @Transactional
    public void removerHorario(Long contaId, Long horarioId) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);
        HorarioDisponivel horario = buscarHorarioDoHemocentro(horarioId, hemocentro.getId());

        if (agendamentoService.temAgendamentosAtivosNoHorario(horario.getId())) {
            throw new HorarioComAgendamentosAtivosException();
        }

        horarioDisponivelRepository.delete(horario);
    }

    public List<HorarioDisponivelResponse> listarHorarios(Long contaId, LocalDate inicio, LocalDate fim) {
        Hemocentro hemocentro = buscarHemocentroPorContaId(contaId);

        return horarioDisponivelRepository
                .findByHemocentroIdAndDataBetween(hemocentro.getId(), inicio, fim)
                .stream()
                .map(HorarioDisponivelResponse::from)
                .toList();
    }

    public Hemocentro buscarEntidadePorContaId(Long contaId) {
        return buscarHemocentroPorContaId(contaId);
    }

    public List<HemocentroResponse> listarTodos() {
        return hemocentroRepository.findAll()
                .stream()
                .map(HemocentroResponse::from)
                .toList();
    }

    public List<HorarioDisponivelResponse> listarHorariosDisponiveisPorData(Long hemocentroId, LocalDate data) {
        return horarioDisponivelRepository
                .findByHemocentroIdAndDataAndDisponivelTrue(hemocentroId, data)
                .stream()
                .map(HorarioDisponivelResponse::from)
                .toList();
    }

    private Hemocentro buscarHemocentroPorContaId(Long contaId) {
        return hemocentroRepository.findByContaId(contaId)
                .orElseThrow(HemocentroNaoEncontradoException::new);
    }

    private HorarioDisponivel buscarHorarioDoHemocentro(Long horarioId, Long hemocentroId) {
        HorarioDisponivel horario = horarioDisponivelRepository.findById(horarioId)
                .orElseThrow(HorarioDisponivelNaoEncontradoException::new);

        if (!horario.getHemocentro().getId().equals(hemocentroId)) {
            throw new HorarioNaoPertenceAoHemocentroException();
        }

        return horario;
    }

    private HorarioDisponivelResponse salvarHorarioTratandoDuplicidade(HorarioDisponivel horario) {
        try {
            return HorarioDisponivelResponse.from(horarioDisponivelRepository.saveAndFlush(horario));
        } catch (DataIntegrityViolationException e) {
            throw new HorarioDisponivelDuplicadoException();
        }
    }
}
