package com.github.sangueamigo.infrastructure.exception;

public class HorarioIndisponivelException extends RuntimeException {
    public HorarioIndisponivelException() {
        super("Horário indisponível ou sem vagas.");
    }
}