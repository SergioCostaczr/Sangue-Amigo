package com.github.sangueamigo.modules.conta.exception;

public class CnpjJaCadastradoException extends RuntimeException {
    public CnpjJaCadastradoException() {
        super("Este CNJP já está cadastrado.");
    }
}
