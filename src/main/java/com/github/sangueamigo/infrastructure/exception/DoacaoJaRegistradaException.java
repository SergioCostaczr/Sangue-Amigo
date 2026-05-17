package com.github.sangueamigo.infrastructure.exception;

public class DoacaoJaRegistradaException extends RuntimeException {
    public DoacaoJaRegistradaException() {
        super("Esta doação já foi registrada anteriormente.");
    }
}
