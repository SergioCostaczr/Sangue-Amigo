package com.github.sangueamigo.infrastructure.exception;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsuarioInaptoException extends RuntimeException {
    public UsuarioInaptoException(LocalDate liberadoEm) {
        super("Você poderá realizar uma nova doação a partir de "
                + liberadoEm.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
    }
}