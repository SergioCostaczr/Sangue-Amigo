package com.github.sangueamigo.modules.agendamento;

import com.github.sangueamigo.modules.agendamento.dto.AgendamentoResponse;
import com.github.sangueamigo.modules.agendamento.dto.CriarAgendamentoRequest;
import com.github.sangueamigo.modules.agendamento.dto.ValidacaoQrCodeResponse;
import com.github.sangueamigo.modules.agendamento.dto.ValidarQrCodeRequest;
import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import com.github.sangueamigo.modules.agendamento.enums.StatusAgendamento;
import com.github.sangueamigo.modules.agendamento.exception.*;
import com.github.sangueamigo.modules.agendamento.repository.AgendamentoRepository;
import com.github.sangueamigo.modules.doacao.entity.Doacao;
import com.github.sangueamigo.modules.doacao.service.DoacaoService;
import com.github.sangueamigo.modules.horariodisponivel.entity.HorarioDisponivel;
import com.github.sangueamigo.modules.horariodisponivel.repository.HorarioDisponivelRepository;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.enums.Sexo;
import com.github.sangueamigo.modules.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final HorarioDisponivelRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DoacaoService doacaoService;


    // RF03, RF06, RF20
    @Transactional
    public AgendamentoResponse criar(Long contaId, CriarAgendamentoRequest request){
        Usuario usuario = usuarioRepository.findByContaId(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));

        HorarioDisponivel horario = horarioRepository.findById(request.horarioId())
                .orElseThrow(() -> new HorarioIndisponivelException());

        validarDisponibilidadeHorario(horario);
        validarIntervaloMinimo(usuario);

        Agendamento agendamento = new Agendamento();
        agendamento.setUsuario(usuario);
        agendamento.setHemocentro(horario.getHemocentro());
        agendamento.setHorarioDisponivel(horario);
        agendamento.setData(horario.getData());
        agendamento.setHorario(horario.getHora());
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        agendamentoRepository.save(agendamento);

        atualizarVagasHorario(horario);

        return AgendamentoResponse.from(agendamento);

    }

    // RF14
    @Transactional
    public AgendamentoResponse confirmar(Long agendamentoId, Long contaId) {

        Agendamento agendamento = buscarPorIdEValidarDono(agendamentoId, contaId);

        if (agendamento.getStatus() != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException(
                    "Apenas agendamentos PENDENTES podem ser confirmados."
            );
        }

        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamento.setQrCodeToken(UUID.randomUUID().toString());

        return AgendamentoResponse.from(agendamentoRepository.save(agendamento));
    }

    //RF05
    @Transactional
    public void cancelar(Long agendamentoId, Long contaId) {

        Agendamento agendamento = buscarPorIdEValidarDono(agendamentoId, contaId);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new IllegalStateException(
                    "Agendamentos concluídos não podem ser cancelados."
            );
        }
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new IllegalStateException("Agendamento já cancelado.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);

        // Devolve a vaga ao horário
        devolverVaga(agendamento.getHorarioDisponivel());
    }

    @Transactional
    public ValidacaoQrCodeResponse validarQrCode(ValidarQrCodeRequest request) {

        Agendamento agendamento = agendamentoRepository
                .findByQrCodeToken(request.qrCodeToken())
                .orElseThrow(QrCodeInvalidoException::new);

        if (agendamento.getStatus() != StatusAgendamento.CONFIRMADO) {
            throw new QrCodeInvalidoException();
        }

        if (doacaoService.jaRegistrada(agendamento.getId())) {
            throw new DoacaoJaRegistradaException();
        }

        Doacao doacao = doacaoService.registrar(agendamento);

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamentoRepository.save(agendamento);

        Usuario usuario = agendamento.getUsuario();
        return new ValidacaoQrCodeResponse(
                doacao.getId(),
                usuario.getNome(),
                usuario.getTipoSanguineo().name(),
                doacao.getDataDoacao()
        );
    }

    // RF04 Consultas de histórico
    public List<AgendamentoResponse> listarTodosDoUsuario(Long contaId) {
        Usuario usuario = usuarioRepository.findByContaId(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        return agendamentoRepository
                .findByUsuarioIdOrderByDataDesc(usuario.getId())
                .stream()
                .map(AgendamentoResponse::from)
                .toList();
    }

    public List<AgendamentoResponse> listarAtivosDoUsuario(Long contaId) {
        Usuario usuario = usuarioRepository.findByContaId(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        return agendamentoRepository
                .findByUsuarioIdAndStatusIn(
                        usuario.getId(),
                        List.of(StatusAgendamento.PENDENTE, StatusAgendamento.CONFIRMADO)
                )
                .stream()
                .map(AgendamentoResponse::from)
                .toList();
    }

    // RF16/RF17 Painel do hemocentro
    public List<AgendamentoResponse> listarPorHemocentro(Long hemocentroId, LocalDate data) {
        return agendamentoRepository
                .findByHemocentroIdAndDataOrderByHorario(hemocentroId, data)
                .stream()
                .map(AgendamentoResponse::from)
                .toList();
    }


    // Metodos de validacao

    private void validarDisponibilidadeHorario(HorarioDisponivel horario){
        if (!horario.getDisponivel()){
            throw new HorarioIndisponivelException();
        }

        long vagasOcupadas = agendamentoRepository.countAgendamentosAtivosNoHorario(horario.getId());

        if (vagasOcupadas >= horario.getVagas()){
            throw new HorarioIndisponivelException();
        }
    }

    private void validarIntervaloMinimo(Usuario usuario) {
        Optional<Agendamento> ultima = agendamentoRepository
                .findTopByUsuarioIdAndStatusOrderByDataDesc(
                        usuario.getId(), StatusAgendamento.CONCLUIDO
                );

        ultima.ifPresent(ag -> {
            int diasMinimos = usuario.getSexo() == Sexo.MASCULINO ? 60 : 90;
            LocalDate liberadoEm = ag.getData().plusDays(diasMinimos);

            if (LocalDate.now().isBefore(liberadoEm)) {
                throw new UsuarioInaptoException(liberadoEm);
            }
        });
    }

    private void atualizarVagasHorario(HorarioDisponivel horario) {
        long vagasOcupadas = agendamentoRepository
                .countAgendamentosAtivosNoHorario(horario.getId());

        if (vagasOcupadas >= horario.getVagas()) {
            horario.setDisponivel(false);
            horarioRepository.save(horario);
        }
    }

    private Agendamento buscarPorIdEValidarDono(Long agendamentoId, Long contaId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(agendamentoId));

        if (!agendamento.getUsuario().getConta().getId().equals(contaId)) {
            throw new AgendamentoNaoPertenceAoUsuarioException();
        }

        return agendamento;
    }

    private void devolverVaga(HorarioDisponivel horario) {
        if (!horario.getDisponivel()) {
            horario.setDisponivel(true);
            horarioRepository.save(horario);
        }
    }

}
