package com.github.sangueamigo.modules.usuario.repository;

import com.github.sangueamigo.modules.usuario.entity.Usuario;
import com.github.sangueamigo.modules.usuario.enums.TipoSanguineo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    // RF 01
    boolean existsByCpf(String cpf);

    Optional<Usuario> findByContaId(Long contaId);

    // RF 13
    List<Usuario> findByTipoSanguineoIn(List<TipoSanguineo> lista);
}
