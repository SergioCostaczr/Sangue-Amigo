package com.github.sangueamigo.modules.usuario.controller;

import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.usuario.dto.request.AtualizarUsuarioRequest;
import com.github.sangueamigo.modules.usuario.dto.response.UsuarioResponse;
import com.github.sangueamigo.modules.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // RF09 Visualizar perfil
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<UsuarioResponse> buscarPerfil(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(usuarioService.buscarPerfil(conta.getId()));
    }

    // RF09 Editar perfil
    @PutMapping("/perfil")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<UsuarioResponse> atualizarPerfil(
            @AuthenticationPrincipal Conta conta,
            @RequestBody @Valid AtualizarUsuarioRequest request
    ) {
        return ResponseEntity.ok(usuarioService.atualizarPerfil(conta.getId(), request));
    }
}