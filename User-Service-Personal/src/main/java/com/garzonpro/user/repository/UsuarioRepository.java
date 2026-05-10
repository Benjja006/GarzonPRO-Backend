package com.garzonpro.user.repository;

import com.garzonpro.user.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importante


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Cambiamos Optional por List porque un rol lo tienen varias personas
    List<Usuario> findByRol(String rol);
}