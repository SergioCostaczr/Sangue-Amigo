package com.github.sangueamigo.modules.campanha.controller;

import com.github.sangueamigo.modules.campanha.dto.request.AtualizarCampanhaRequest;
import com.github.sangueamigo.modules.campanha.dto.request.CriarCampanhaRequest;
import com.github.sangueamigo.modules.campanha.dto.response.CampanhaResponse;
import com.github.sangueamigo.modules.campanha.service.CampanhaService;
import com.github.sangueamigo.modules.conta.entity.Conta;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campanhas")
@RequiredArgsConstructor
public class CampanhaController {

    private final CampanhaService campanhaService;

    // RF08 Listar campanhas ativas (público)
    @GetMapping
    public ResponseEntity<List<CampanhaResponse>> listarAtivas() {
        return ResponseEntity.ok(campanhaService.listarAtivas());
    }

    // Listar campanhas do hemocentro autenticado
    @GetMapping("/minhas")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<List<CampanhaResponse>> listarMinhas(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(campanhaService.listarDoHemocentro(conta.getId()));
    }

    // Buscar campanha por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<CampanhaResponse> buscarPorId(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(campanhaService.buscarPorId(conta.getId(), id));
    }

    // Criar campanha
    @PostMapping
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<CampanhaResponse> criar(
            @AuthenticationPrincipal Conta conta,
            @RequestBody @Valid CriarCampanhaRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campanhaService.criar(conta.getId(), request));
    }

    // Atualizar campanha
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<CampanhaResponse> atualizar(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id,
            @RequestBody @Valid AtualizarCampanhaRequest request
    ) {
        return ResponseEntity.ok(campanhaService.atualizar(conta.getId(), id, request));
    }

    // Remover campanha
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<Void> remover(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        campanhaService.remover(conta.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
