package com.github.sangueamigo.modules.conta.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException() {
        super("Este CPF já está cadastrado.");
    }
}
