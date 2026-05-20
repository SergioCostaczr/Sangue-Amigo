package com.github.sangueamigo.modules.conta.service;

import com.github.sangueamigo.infrastructure.security.JwtService;
import com.github.sangueamigo.modules.conta.dto.request.*;
import com.github.sangueamigo.modules.conta.dto.response.AuthResponse;
import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.conta.enums.Role;
import com.github.sangueamigo.modules.conta.exception.*;
import com.github.sangueamigo.modules.conta.repository.ContaRepository;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.repository.HemocentroRepository;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HemocentroRepository hemocentroRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // RF01 Cadastro
    @Transactional
    public void cadastrarUsuario(CadastrarUsuarioRequest request){

        if (contaRepository.findByEmail(request.email()).isPresent()){
            throw new EmailJaCadastradoException();
        }

        if (usuarioRepository.existsByCpf(request.cpf())){
            throw new CpfJaCadastradoException();
        }

        Conta conta = new Conta();
        conta.setEmail(request.email());
        conta.setSenha(passwordEncoder.encode(request.senha()));
        conta.setRole(Role.ROLE_USUARIO);
        contaRepository.save(conta);

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setTelefone(request.telefone());
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setTipoSanguineo(request.tipoSanguineo());
        usuario.setSexo(request.sexo());
        usuario.setConta(conta);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cadastrarHemocentro(CadastrarHemocentroRequest request) {

        if (contaRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailJaCadastradoException();
        }

        if (hemocentroRepository.existsBYCnpj(request.cnpj())){
            throw new CnpjJaCadastradoException();
        }

        Conta conta = new Conta();
        conta.setEmail(request.email());
        conta.setSenha(passwordEncoder.encode(request.senha()));
        conta.setRole(Role.ROLE_HEMOCENTRO);
        contaRepository.save(conta);

        Hemocentro hemocentro = new Hemocentro();
        hemocentro.setNome(request.nome());
        hemocentro.setTelefone(request.telefone());
        hemocentro.setEndereco(request.endereco());
        hemocentro.setCidade(request.cidade());
        hemocentro.setEstado(request.estado());
        hemocentro.setConta(conta);
        hemocentroRepository.save(hemocentro);
    }

    // RF02 Login
    public AuthResponse login(LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );
        } catch (AuthenticationException e){
            throw new CredenciaisInvalidasException();
        }

        Conta conta = contaRepository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException());

        String accessToken = jwtService.gerarAccessToken(conta);
        String refreshToken = jwtService.gerarRefreshToken(conta);

        return new AuthResponse(accessToken,refreshToken,conta.getRole().name());
    }

    // Refresh token
    public AuthResponse refresh(RefreshTokenRequest request) {
        String email = jwtService.extrairEmail(request.refreshToken());

        Conta conta = contaRepository.findByEmail(email)
                .orElseThrow(TokenInvalidoException::new);

        if (!jwtService.isTokenValido(request.refreshToken(), conta)) {
            throw new TokenInvalidoException();
        }
        String novoAccessToken = jwtService.gerarAccessToken(conta);
        return new AuthResponse(novoAccessToken, request.refreshToken(), conta.getRole().name());
    }

    public void solicitarRecuperacaoSenha(RecuperarSenhaRequest request){
        contaRepository.findByEmail(request.email()).ifPresent(conta -> {
            String resetToken = jwtService.generateResetToken(conta);
            // notificacao service envia email com token
        });
    }

}
