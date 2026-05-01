package com.github.sangueamigo.modules.conta.repository;

import com.github.sangueamigo.modules.conta.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta,Long> {
    Optional<Conta> findByEmail(String email);

}
