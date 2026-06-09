package com.github.sangueamigo.modules.conta.controller;

import com.github.sangueamigo.modules.conta.dto.request.*;
import com.github.sangueamigo.modules.conta.dto.response.AuthResponse;
import com.github.sangueamigo.modules.conta.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ContaService contaService;


    // RF01 Cadastro de usuário
    @PostMapping("/cadastro-usuario")
    public ResponseEntity<Void> cadastrarUsuario(
            @RequestBody @Valid CadastrarUsuarioRequest request
    ) {
        contaService.cadastrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // RF01 Cadastro de hemocentro
    @PostMapping("/cadastro-hemocentro")
    public ResponseEntity<Void> cadastrarHemocentro(
            @RequestBody @Valid CadastrarHemocentroRequest request
    ) {
        contaService.cadastrarHemocentro(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // RF02 Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(contaService.login(request));
    }


    // Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(contaService.refresh(request));
    }

    // RF21 Recuperação de senha passo 1
    @PostMapping("/recuperar-senha")
    public ResponseEntity<Void> recuperarSenha(
            @RequestBody @Valid RecuperarSenhaRequest request
            ) {
        contaService.solicitarRecuperacaoSenha(request);
        return ResponseEntity.ok().build();

    }

    // RF21 Recuperação de senha passo 2
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @RequestBody @Valid RedefinirSenhaRequest request
    ) {
        contaService.redefinirSenha(request);
        return ResponseEntity.ok().build();
    }
}
