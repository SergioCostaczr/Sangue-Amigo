package com.github.sangueamigo.modules.hemocentro.exception;

public class HorarioDisponivelDuplicadoException extends RuntimeException {
    public HorarioDisponivelDuplicadoException() {
        super("Ja existe um horario cadastrado para este hemocentro na mesma data e hora.");
    }
}
