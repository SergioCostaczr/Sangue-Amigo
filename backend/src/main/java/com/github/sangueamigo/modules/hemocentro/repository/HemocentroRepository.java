package com.github.sangueamigo.modules.hemocentro.repository;

import com.github.sangueamigo.modules.hemocentro.entity.Hemocentro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HemocentroRepository extends JpaRepository<Hemocentro,Long> {
    Optional<Hemocentro> findByContaId(Long contaId);

    boolean existsByCnpj(String cnpj);

}
