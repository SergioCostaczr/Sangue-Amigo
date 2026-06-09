package com.github.sangueamigo.modules.agendamento.exception;

public class DoacaoJaRegistradaException extends RuntimeException {
    public DoacaoJaRegistradaException() {
        super("Esta doação já foi registrada anteriormente.");
    }
}
