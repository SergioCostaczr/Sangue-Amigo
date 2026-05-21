package com.github.sangueamigo.modules.campanha.exception;

public class CampanhaNaoEncontradaException extends RuntimeException {
    public CampanhaNaoEncontradaException() {
        super("Campanha nao encontrada.");
    }
}
