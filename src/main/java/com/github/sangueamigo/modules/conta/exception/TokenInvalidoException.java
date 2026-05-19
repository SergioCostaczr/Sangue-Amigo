package com.github.sangueamigo.modules.conta.exception;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String message) {
        super("Token inválido ou expirado.");
    }
}
