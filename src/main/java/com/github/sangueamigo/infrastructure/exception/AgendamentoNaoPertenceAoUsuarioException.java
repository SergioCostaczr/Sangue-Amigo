package com.github.sangueamigo.infrastructure.exception;

public class AgendamentoNaoPertenceAoUsuarioException extends RuntimeException {
    public AgendamentoNaoPertenceAoUsuarioException() {
        super("Este agendamento não pertence ao usuário autenticado.");
    }
}
