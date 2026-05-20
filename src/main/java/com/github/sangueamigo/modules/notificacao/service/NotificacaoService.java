package com.github.sangueamigo.modules.notificacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviarBoasVindas(String email, String nome){
        enviar(
                email,
                "Bem-vindo ao Sangue Amigo",
                "Olá, " + nome + "!\n\nSeu cadastro foi realizado com sucesso.\n\nObrigado por fazer parte do Sangue Amigo."

        );
    }

    private void enviar(String destinatario, String assunto, String corpo){
        SimpleMailMessage menssagem = new SimpleMailMessage();
        menssagem.setFrom(remetente);
        menssagem.setTo(destinatario);
        menssagem.setSubject(assunto);
        menssagem.setText(corpo);
        mailSender.send(menssagem);
    }
}
