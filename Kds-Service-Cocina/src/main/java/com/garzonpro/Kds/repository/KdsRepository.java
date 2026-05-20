package com.garzonpro.Kds.repository;

import com.garzonpro.Kds.model.TicketCocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KdsRepository extends JpaRepository<TicketCocina, Long> {
    // Cambiamos "Estado" por "EstadoGeneral"
    List<TicketCocina> findByEstadoGeneralNot(String estadoGeneral);
}