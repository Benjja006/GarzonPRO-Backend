package com.garzonpro.Status.repository;

import com.garzonpro.Status.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByIdGarzonDestino(Long idGarzon);
    List<Notificacion> findByIdGarzonDestinoAndLeidoFalse(long idGarzon);
}