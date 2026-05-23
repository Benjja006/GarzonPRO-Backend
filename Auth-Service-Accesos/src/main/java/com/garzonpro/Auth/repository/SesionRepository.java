package com.garzonpro.Auth.repository;

import com.garzonpro.Auth.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    Optional<Sesion> findFirstByIdUsuarioAndFechaFinAfter(Long idUsuario, LocalDateTime fecha);
}