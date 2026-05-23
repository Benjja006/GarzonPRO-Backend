package com.garzonpro.Status.repository;

import com.garzonpro.Status.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Todas las notificaciones de un garzón (leídas + pendientes)
    List<Notificacion> findByIdGarzonDestino(Long idGarzonDestino);

    // Solo las notificaciones no leídas de un garzón
    List<Notificacion> findByIdGarzonDestinoAndLeidoFalse(Long idGarzonDestino);
}