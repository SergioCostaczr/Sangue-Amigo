package com.github.sangueamigo.modules.campanha.exception;

public class PeriodoCampanhaInvalidoException extends RuntimeException {
    public PeriodoCampanhaInvalidoException() {
        super("A data final da campanha nao pode ser anterior a data inicial.");
    }
}
