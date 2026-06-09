package com.github.sangueamigo.modules.agendamento.exception;

public class HorarioIndisponivelException extends RuntimeException {
    public HorarioIndisponivelException() {
        super("Horário indisponível ou sem vagas.");
    }
}