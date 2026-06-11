package com.github.sangueamigo.modules.usuario.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("Usuario nao encontrado.");
    }
}
