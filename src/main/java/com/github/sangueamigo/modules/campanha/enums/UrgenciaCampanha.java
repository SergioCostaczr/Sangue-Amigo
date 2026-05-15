package com.github.sangueamigo.modules.campanha.enums;

public enum UrgenciaCampanha {
    NORMAL,    // apenas exibição na listagem do front, sem notificação automática
    ALTA,      // notifica usuários compatíveis aptos a doar
    CRITICA    // notifica todos os compatíveis + disparo imediato ao atualizar urgência
}
