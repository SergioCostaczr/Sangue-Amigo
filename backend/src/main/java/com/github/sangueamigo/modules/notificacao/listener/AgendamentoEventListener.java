package com.github.sangueamigo.modules.notificacao.listener;

import com.github.sangueamigo.modules.agendamento.event.AgendamentoCanceladoEvent;
import com.github.sangueamigo.modules.agendamento.event.AgendamentoConfirmadoEvent;
import com.github.sangueamigo.modules.agendamento.event.DoacaoRegistradaEvent;
import com.github.sangueamigo.modules.notificacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AgendamentoEventListener {

    private final NotificacaoService notificacaoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAgendamentoConfirmado(AgendamentoConfirmadoEvent event) {
        notificacaoService.enviarAgendamentoConfirmado(
                event.email(),
                event.nomeUsuario(),
                event.nomeHemocentro(),
                event.data(),
                event.horario(),
                event.qrCodeToken()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAgendamentoCancelado(AgendamentoCanceladoEvent event) {
        notificacaoService.enviarAgendamentoCancelado(
                event.email(),
                event.nomeUsuario(),
                event.nomeHemocentro(),
                event.data(),
                event.horario()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDoacaoRegistrada(DoacaoRegistradaEvent event) {
        notificacaoService.enviarDoacaoRegistrada(
                event.email(),
                event.nomeUsuario(),
                event.nomeHemocentro(),
                event.dataDoacao()
        );
    }
}
