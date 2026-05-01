package com.github.sangueamigo.infrastructure.security;

import com.github.sangueamigo.modules.conta.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ContaRepository contaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return contaRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Conta não encontrada para o email: " + email)
        );
    }
}
