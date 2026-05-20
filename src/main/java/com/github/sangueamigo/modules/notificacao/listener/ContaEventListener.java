package com.github.sangueamigo.modules.notificacao.listener;

import com.github.sangueamigo.modules.conta.event.UsuarioCadastradoEvent;
import com.github.sangueamigo.modules.notificacao.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ContaEventListener {
    private final NotificacaoService notificacaoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUsuarioCadastro(UsuarioCadastradoEvent event){
        notificacaoService.enviarBoasVindas(event.email(),event.nome());
    }
}
