package com.github.sangueamigo.modules.hemocentro.exception;

public class HorarioDisponivelNaoEncontradoException extends RuntimeException {
    public HorarioDisponivelNaoEncontradoException() {
        super("Horario disponivel nao encontrado.");
    }
}
