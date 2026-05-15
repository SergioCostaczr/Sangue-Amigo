package com.github.sangueamigo.modules.campanha.enums;

public enum StatusCampanha {
    AGENDADA,    // dataInicio ainda não chegou
    ATIVA,       // entre dataInicio e dataFim
    ENCERRADA    // dataFim passou, ou encerrada manualmente pelo hemocentro
}