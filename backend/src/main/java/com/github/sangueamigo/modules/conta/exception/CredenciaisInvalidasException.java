package com.github.sangueamigo.modules.conta.exception;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos.");
    }
}
