package com.github.sangueamigo.modules.notificacao.listener;

import com.github.sangueamigo.modules.campanha.event.CampanhaUrgenciaCriticaEvent;
import com.github.sangueamigo.modules.campanha.event.DestinatarioCampanhaCritica;
import com.github.sangueamigo.modules.notificacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CampanhaEventListener {

    private final NotificacaoService notificacaoService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCampanhaUrgenciaCritica(CampanhaUrgenciaCriticaEvent event) {
        for (DestinatarioCampanhaCritica destinatario : event.destinatarios()) {
            notificacaoService.enviarCampanhaUrgenciaCritica(
                    destinatario.email(),
                    destinatario.nome(),
                    event.titulo(),
                    event.nomeHemocentro(),
                    event.dataInicio(),
                    event.dataFim(),
                    event.endereco(),
                    event.cidade(),
                    event.estado()
            );
        }
    }
}
