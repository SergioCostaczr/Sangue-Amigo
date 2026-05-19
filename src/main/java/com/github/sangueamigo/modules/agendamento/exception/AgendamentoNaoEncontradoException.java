package com.github.sangueamigo.modules.agendamento.exception;

public class AgendamentoNaoEncontradoException extends RuntimeException {
    public AgendamentoNaoEncontradoException(Long id) {
        super("Agendamento não encontrado: " + id);
    }
}
