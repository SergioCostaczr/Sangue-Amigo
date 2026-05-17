package com.github.sangueamigo.modules.campanha.repository;

import com.github.sangueamigo.modules.campanha.entity.Campanha;
import com.github.sangueamigo.modules.campanha.enums.StatusCampanha;
import com.github.sangueamigo.modules.campanha.enums.UrgenciaCampanha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

    // Listar campanhas ativas para exibição pública (RF07/RF08)
    List<Campanha> findByStatusOrderByUrgenciaDesc(StatusCampanha status);

    // Campanhas do hemocentro (painel do hemocentro)
    List<Campanha> findByHemocentroIdOrderByDataInicioDesc(Long hemocentroId);

    // Scheduler para ativar campanhas cuja dataInicio chegou
    List<Campanha> findByStatusAndDataInicioLessThanEqual(
            StatusCampanha status, LocalDate data
    );

    // Scheduler para encerrar campanhas cuja dataFim passou
    List<Campanha> findByStatusAndDataFimBefore(
            StatusCampanha status, LocalDate data
    );

    // Scheduler de notificações. Campanhas ativas por nível de urgência (RF13)
    List<Campanha> findByStatusAndUrgencia(
            StatusCampanha status, UrgenciaCampanha urgencia
    );
}
