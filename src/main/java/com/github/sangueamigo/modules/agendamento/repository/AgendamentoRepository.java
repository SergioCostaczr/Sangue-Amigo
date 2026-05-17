package com.github.sangueamigo.modules.agendamento.repository;

import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import com.github.sangueamigo.modules.agendamento.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // RF 04
    List<Agendamento> findByUsuarioIdOrderByDataDesc(Long id);

    // Agendamentos ativos do usuário - RF05
    List<Agendamento> findByUsuarioIdAndStatusIn(
            Long usuarioId, List<StatusAgendamento> statuses
    );

    // RF 06
    Optional<Agendamento> findTopByUsuarioIdAndStatusOrderByDataDesc(
            Long usuarioId, StatusAgendamento status
    );

    // RF 15
    Optional<Agendamento> findByQrCodeToken(String qrCodeToken);

    // Contar agendamentos ativos num horário - RF20
    @Query("""
        SELECT COUNT(a) FROM Agendamento a
        WHERE a.horarioDisponivel.id = :horarioId
        AND a.status NOT IN ('CANCELADO')
    """)
    long countAgendamentosAtivosNoHorario(@Param("horarioId") Long horarioId);

    // RF16/RF17
    List<Agendamento> findByHemocentroIdAndDataOrderByHorario(
            Long hemocentroId, LocalDate data
    );
}
