package com.github.sangueamigo.modules.hemocentro.exception;

public class HorarioComAgendamentosAtivosException extends RuntimeException {
    public HorarioComAgendamentosAtivosException() {
        super("Nao e possivel remover um horario com agendamentos ativos.");
    }
}
