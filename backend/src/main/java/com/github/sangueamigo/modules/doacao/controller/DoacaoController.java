package com.github.sangueamigo.modules.doacao.controller;

import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.doacao.dto.DoacaoResponse;
import com.github.sangueamigo.modules.doacao.service.DoacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
@RequiredArgsConstructor
public class DoacaoController {

    private final DoacaoService doacaoService;

    // RF04 Histórico de doações do usuário
    @GetMapping("/historico")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<List<DoacaoResponse>> listarHistoricoUsuario(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(doacaoService.listarHistoricoDoUsuario(conta.getId()));
    }

    // RF17 Histórico de doações do hemocentro
    @GetMapping("/hemocentro")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<List<DoacaoResponse>> listarHistoricoHemocentro(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(doacaoService.listarHistoricoDoHemocentro(conta.getId()));
    }
}