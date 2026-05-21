package com.github.sangueamigo.modules.campanha.exception;

public class CampanhaNaoPertenceAoHemocentroException extends RuntimeException {
    public CampanhaNaoPertenceAoHemocentroException() {
        super("Campanha nao pertence ao hemocentro autenticado.");
    }
}
