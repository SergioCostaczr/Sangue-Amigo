package com.github.sangueamigo.modules.usuario.repository;

import com.github.sangueamigo.modules.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
