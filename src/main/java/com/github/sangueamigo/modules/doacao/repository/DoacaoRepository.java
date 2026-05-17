package com.github.sangueamigo.modules.doacao.repository;

import com.github.sangueamigo.modules.doacao.entity.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoacaoRepository extends JpaRepository<Doacao,Long> {

    // Historico de doações (RF04)
    List<Doacao> findByAgendamentoUsuarioIdOrderByDataDoacaoDesc(Long usuarioId);

    // Histórico geral pelo admin (RF17)
    List<Doacao> findByAgendamentoHemocentroIdOrderByDataDoacaoDesc(Long hemocentroId);

    // evitar duplicidade (RF15)
    boolean existsByAgendamentoId(Long agendamentoId);

}
