package com.github.sangueamigo.modules.hemocentro.exception;

public class HemocentroNaoEncontradoException extends RuntimeException {
    public HemocentroNaoEncontradoException() {
        super("Hemocentro nao encontrado.");
    }
}
