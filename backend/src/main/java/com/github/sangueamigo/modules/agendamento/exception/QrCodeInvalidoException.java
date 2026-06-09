package com.github.sangueamigo.modules.agendamento.exception;

public class QrCodeInvalidoException extends RuntimeException {
    public QrCodeInvalidoException() {
        super("QR Code inválido ou agendamento não encontrado.");
    }
}