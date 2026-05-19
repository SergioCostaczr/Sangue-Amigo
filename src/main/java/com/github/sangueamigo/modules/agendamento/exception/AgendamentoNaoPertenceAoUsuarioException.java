package com.github.sangueamigo.modules.agendamento.exception;

public class AgendamentoNaoPertenceAoUsuarioException extends RuntimeException {
    public AgendamentoNaoPertenceAoUsuarioException() {
        super("Este agendamento não pertence ao usuário autenticado.");
    }
}
