package com.github.sangueamigo.modules.agendamento;

import com.github.sangueamigo.infrastructure.exception.HorarioIndisponivelException;
import com.github.sangueamigo.infrastructure.exception.UsuarioInaptoException;
import com.github.sangueamigo.modules.agendamento.dto.AgendamentoResponse;
import com.github.sangueamigo.modules.agendamento.dto.CriarAgendamentoRequest;
import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import com.github.sangueamigo.modules.agendamento.enums.StatusAgendamento;
import com.github.sangueamigo.modules.agendamento.repository.AgendamentoRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final HorarioDisponivelRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;

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


}
