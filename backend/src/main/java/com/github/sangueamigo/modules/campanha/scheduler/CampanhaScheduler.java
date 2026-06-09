package com.github.sangueamigo.modules.campanha.scheduler;

import com.github.sangueamigo.modules.campanha.entity.Campanha;
import com.github.sangueamigo.modules.campanha.enums.StatusCampanha;
import com.github.sangueamigo.modules.campanha.repository.CampanhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CampanhaScheduler {

    private final CampanhaRepository campanhaRepository;

    @Scheduled(cron = "0 0 0 * * *") // meia-noite todo dia
    @Transactional
    public void atualizarStatusCampanhas() {
        LocalDate hoje = LocalDate.now();

        // AGENDADA -> ATIVA
        List<Campanha> paraAtivar = campanhaRepository
                .findByStatusAndDataInicioLessThanEqual(StatusCampanha.AGENDADA, hoje);

        paraAtivar.forEach(c -> c.setStatus(StatusCampanha.ATIVA));
        campanhaRepository.saveAll(paraAtivar);

        // ATIVA -> ENCERRADA
        List<Campanha> paraEncerrar = campanhaRepository
                .findByStatusAndDataFimBefore(StatusCampanha.ATIVA, hoje);

        paraEncerrar.forEach(c -> c.setStatus(StatusCampanha.ENCERRADA));
        campanhaRepository.saveAll(paraEncerrar);
    }
}