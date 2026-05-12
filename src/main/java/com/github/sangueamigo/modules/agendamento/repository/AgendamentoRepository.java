package com.github.sangueamigo.modules.agendamento.repository;

import com.github.sangueamigo.modules.agendamento.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
