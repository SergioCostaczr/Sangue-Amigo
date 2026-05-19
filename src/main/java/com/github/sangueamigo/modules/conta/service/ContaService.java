package com.github.sangueamigo.modules.conta.service;

import com.github.sangueamigo.infrastructure.security.JwtService;
import com.github.sangueamigo.modules.conta.dto.request.CadastrarHemocentroRequest;
import com.github.sangueamigo.modules.conta.dto.request.CadastrarUsuarioRequest;
import com.github.sangueamigo.modules.conta.entity.Conta;
import com.github.sangueamigo.modules.conta.enums.Role;
import com.github.sangueamigo.modules.conta.exception.CnpjJaCadastradoException;
import com.github.sangueamigo.modules.conta.exception.CpfJaCadastradoException;
import com.github.sangueamigo.modules.conta.exception.EmailJaCadastradoException;
import com.github.sangueamigo.modules.conta.repository.ContaRepository;
import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import com.github.sangueamigo.modules.hemocentro.repository.HemocentroRepository;
import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
}
