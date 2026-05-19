package com.github.sangueamigo.modules.conta.exception;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String message) {
        super("Este e-mail já está cadastrado.");
    }
}
