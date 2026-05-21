package com.github.sangueamigo.modules.notificacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

    public void enviarRecuperacaoSenha(String email, String resetToken) {
        String link = "https://sangueamigo.com/redefinir-senha?token=" + resetToken;
        enviar(
                email,
                "Recuperação de senha - Sangue Amigo",
                "Recebemos uma solicitação de recuperação de senha.\n\n" +
                        "Clique no link abaixo para redefinir sua senha (válido por 15 minutos):\n" + link +
                        "\n Caso o link não funcione copie o token abaixo e volte ao site para continuar a redefinição de senha:\n" +
                        resetToken +
                        "\n\nSe não foi você, ignore este e-mail.\n"
        );
    }

    public void enviarAgendamentoConfirmado(
            String email,
            String nomeUsuario,
            String nomeHemocentro,
            LocalDate data,
            LocalTime horario,
            String qrCodeToken
    ) {
        enviar(
                email,
                "Agendamento confirmado - Sangue Amigo",
                "Ola, " + nomeUsuario + "!\n\n" +
                        "Seu agendamento no hemocentro " + nomeHemocentro + " foi confirmado para " +
                        formatarDataHora(data, horario) + ".\n\n" +
                        "Token do QR Code: " + qrCodeToken + "\n\n" +
                        "Apresente esse token no momento da doacao."
        );
    }

    public void enviarAgendamentoCancelado(
            String email,
            String nomeUsuario,
            String nomeHemocentro,
            LocalDate data,
            LocalTime horario
    ) {
        enviar(
                email,
                "Agendamento cancelado - Sangue Amigo",
                "Ola, " + nomeUsuario + "!\n\n" +
                        "Seu agendamento no hemocentro " + nomeHemocentro + " para " +
                        formatarDataHora(data, horario) + " foi cancelado."
        );
    }

    public void enviarDoacaoRegistrada(
            String email,
            String nomeUsuario,
            String nomeHemocentro,
            LocalDate dataDoacao
    ) {
        enviar(
                email,
                "Doacao registrada - Sangue Amigo",
                "Ola, " + nomeUsuario + "!\n\n" +
                        "Sua doacao no hemocentro " + nomeHemocentro + " foi registrada em " +
                        dataDoacao.format(DATA_FORMATTER) + ".\n\n" +
                        "Obrigado por ajudar a salvar vidas."
        );
    }

    public void enviarCampanhaUrgenciaCritica(
            String email,
            String nomeUsuario,
            String tituloCampanha,
            String nomeHemocentro,
            LocalDate dataInicio,
            LocalDate dataFim,
            String endereco,
            String cidade,
            String estado
    ) {
        String local = montarLocal(endereco, cidade, estado);
        enviar(
                email,
                "Campanha critica de doacao - Sangue Amigo",
                "Ola, " + nomeUsuario + "!\n\n" +
                        "A campanha \"" + tituloCampanha + "\" do hemocentro " + nomeHemocentro +
                        " esta com urgencia critica.\n\n" +
                        "Periodo: " + dataInicio.format(DATA_FORMATTER) + " a " + dataFim.format(DATA_FORMATTER) +
                        "\nLocal: " + local +
                        "\n\nSe puder doar, seu sangue pode fazer diferenca agora."
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

    private String formatarDataHora(LocalDate data, LocalTime horario) {
        return data.format(DATA_FORMATTER) + " as " + horario.format(HORA_FORMATTER);
    }

    private String montarLocal(String endereco, String cidade, String estado) {
        StringBuilder local = new StringBuilder();

        if (endereco != null && !endereco.isBlank()) {
            local.append(endereco);
        }
        if (cidade != null && !cidade.isBlank()) {
            if (!local.isEmpty()) {
                local.append(", ");
            }
            local.append(cidade);
        }
        if (estado != null && !estado.isBlank()) {
            if (!local.isEmpty()) {
                local.append(" - ");
            }
            local.append(estado);
        }

        return local.isEmpty() ? "Consulte os detalhes da campanha no Sangue Amigo." : local.toString();
    }
}
