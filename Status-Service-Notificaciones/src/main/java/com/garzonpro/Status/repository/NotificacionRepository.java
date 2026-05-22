package com.garzonpro.Status.repository;

import com.garzonpro.Status.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Aquí podrás agregar métodos personalizados más adelante si los necesitas
}