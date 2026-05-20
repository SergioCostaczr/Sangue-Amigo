package com.github.sangueamigo.modules.horariodisponivel.repository;

import com.github.sangueamigo.modules.horariodisponivel.entity.HorarioDisponivel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {

    // RF 03
    List<HorarioDisponivel> findByHemocentroIdAndDataAndDisponivelTrue(Long homocentroId, LocalDate data);

    // Listar todos os horários de um hemocentro num período (painel admin)
    List<HorarioDisponivel> findByHemocentroIdAndDataBetween(
            Long hemocentroId, LocalDate inicio, LocalDate fim
    );
}
