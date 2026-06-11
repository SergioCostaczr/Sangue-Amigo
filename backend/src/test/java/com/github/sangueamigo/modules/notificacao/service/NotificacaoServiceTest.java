package com.github.sangueamigo.modules.notificacao.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificacaoServiceTest {

    @Test
    void deveEnviarLinkDeRecuperacaoUsandoUrlDoFrontend() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        NotificacaoService service = new NotificacaoService(mailSender);
        ReflectionTestUtils.setField(service, "remetente", "no-reply@sangueamigo.local");
        ReflectionTestUtils.setField(service, "notificacoesHabilitadas", true);
        ReflectionTestUtils.setField(service, "frontendBaseUrl", "http://localhost:5173/");

        service.enviarRecuperacaoSenha("usuario@email.com", "token.teste");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        assertThat(captor.getValue().getText())
                .contains("http://localhost:5173/redefinir-senha?token=token.teste");
    }
}
