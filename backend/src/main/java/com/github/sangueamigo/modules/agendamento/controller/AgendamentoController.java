package com.github.sangueamigo.modules.agendamento.controller;

import com.github.sangueamigo.modules.agendamento.dto.request.CriarAgendamentoRequest;
import com.github.sangueamigo.modules.agendamento.dto.request.ValidarQrCodeRequest;
import com.github.sangueamigo.modules.agendamento.dto.response.AgendamentoResponse;
import com.github.sangueamigo.modules.agendamento.dto.response.ValidacaoQrCodeResponse;
import com.github.sangueamigo.modules.agendamento.service.AgendamentoService;
import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.service.HemocentroService;
import com.github.sangueamigo.modules.qrcode.QrCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final QrCodeService qrCodeService;
    private final HemocentroService hemocentroService;

    // Criar agendamento
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<AgendamentoResponse> criar(
            @AuthenticationPrincipal Conta conta,
            @RequestBody @Valid CriarAgendamentoRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendamentoService.criar(conta.getId(), request));
    }

    // RF14 Confirmar e gerar QR Code
    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<AgendamentoResponse> confirmar(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(agendamentoService.confirmar(id, conta.getId()));
    }

    // RF05 Cancelar agendamento
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<Void> cancelar(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        agendamentoService.cancelar(id, conta.getId());
        return ResponseEntity.noContent().build();
    }

    // RF04 Histórico completo do usuário
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<List<AgendamentoResponse>> listarTodos(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(agendamentoService.listarTodosDoUsuario(conta.getId()));
    }

    // RF04 Agendamentos ativos do usuário
    @GetMapping("/ativos")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<List<AgendamentoResponse>> listarAtivos(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(agendamentoService.listarAtivosDoUsuario(conta.getId()));
    }

    // QR Code em Base64
    @GetMapping("/{id}/qrcode")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<String> obterQrCode(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        String token = agendamentoService.buscarQrCodeToken(id, conta.getId());
        return ResponseEntity.ok(qrCodeService.gerarQrCodeBase64(token));
    }

    // RF15 Validar QR Code (hemocentro)
    @PostMapping("/validar-qrcode")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<ValidacaoQrCodeResponse> validarQrCode(
            @RequestBody @Valid ValidarQrCodeRequest request
    ) {
        return ResponseEntity.ok(agendamentoService.validarQrCode(request));
    }

    // RF16/RF17 Painel do hemocentro
    @GetMapping("/hemocentro")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<List<AgendamentoResponse>> listarPorHemocentro(
            @AuthenticationPrincipal Conta conta,
            @RequestParam LocalDate data
    ) {
        Hemocentro hemocentro = hemocentroService.buscarEntidadePorContaId(conta.getId());
        return ResponseEntity.ok(
                agendamentoService.listarPorHemocentro(hemocentro.getId(), data)
        );
    }
}