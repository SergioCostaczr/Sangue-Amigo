package com.github.sangueamigo.modules.hemocentro.controller;

import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.hemocentro.dto.request.AtualizarHemocentroRequest;
import com.github.sangueamigo.modules.hemocentro.dto.request.AtualizarHorarioDisponivelRequest;
import com.github.sangueamigo.modules.hemocentro.dto.request.CriarHorarioDisponivelRequest;
import com.github.sangueamigo.modules.hemocentro.dto.response.HemocentroResponse;
import com.github.sangueamigo.modules.hemocentro.dto.response.HorarioDisponivelResponse;
import com.github.sangueamigo.modules.hemocentro.service.HemocentroService;
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
@RequestMapping("/hemocentros")
@RequiredArgsConstructor
public class HemocentroController {

    private final HemocentroService hemocentroService;

    // RF07 Listar hemocentros disponíveis (público)
    @GetMapping
    public ResponseEntity<List<HemocentroResponse>> listar() {
        return ResponseEntity.ok(hemocentroService.listarTodos());
    }

    // Visualizar perfil próprio
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<HemocentroResponse> buscarPerfil(
            @AuthenticationPrincipal Conta conta
    ) {
        return ResponseEntity.ok(hemocentroService.buscarPerfil(conta.getId()));
    }

    // Editar perfil
    @PutMapping("/perfil")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<HemocentroResponse> atualizarPerfil(
            @AuthenticationPrincipal Conta conta,
            @RequestBody @Valid AtualizarHemocentroRequest request
    ) {
        return ResponseEntity.ok(hemocentroService.atualizarPerfil(conta.getId(), request));
    }

    // Criar horário disponível
    @PostMapping("/horarios")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<HorarioDisponivelResponse> criarHorario(
            @AuthenticationPrincipal Conta conta,
            @RequestBody @Valid CriarHorarioDisponivelRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hemocentroService.criarHorario(conta.getId(), request));
    }

    // Listar horários por período
    @GetMapping("/horarios")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<List<HorarioDisponivelResponse>> listarHorarios(
            @AuthenticationPrincipal Conta conta,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {
        return ResponseEntity.ok(hemocentroService.listarHorarios(conta.getId(), inicio, fim));
    }

    @GetMapping("/{id}/horarios")
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    public ResponseEntity<List<HorarioDisponivelResponse>> listarHorariosDisponiveis(
            @PathVariable Long id,
            @RequestParam LocalDate data
    ) {
        return ResponseEntity.ok(
                hemocentroService.listarHorariosDisponiveisPorData(id, data)
        );
    }

    // Atualizar horário
    @PutMapping("/horarios/{id}")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<HorarioDisponivelResponse> atualizarHorario(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id,
            @RequestBody @Valid AtualizarHorarioDisponivelRequest request
    ) {
        return ResponseEntity.ok(hemocentroService.atualizarHorario(conta.getId(), id, request));
    }

    // Remover horário
    @DeleteMapping("/horarios/{id}")
    @PreAuthorize("hasRole('ROLE_HEMOCENTRO')")
    public ResponseEntity<Void> removerHorario(
            @AuthenticationPrincipal Conta conta,
            @PathVariable Long id
    ) {
        hemocentroService.removerHorario(conta.getId(), id);
        return ResponseEntity.noContent().build();
    }
}