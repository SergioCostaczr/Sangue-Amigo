package com.github.sangueamigo.modules.conta.exception;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException() {
        super("Este e-mail já está cadastrado.");
    }
}
