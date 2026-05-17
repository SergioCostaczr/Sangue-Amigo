package com.github.sangueamigo.infrastructure.exception;

public class AgendamentoNaoEncontradoException extends RuntimeException {
    public AgendamentoNaoEncontradoException(Long id) {
        super("Agendamento não encontrado: " + id);
    }
}
