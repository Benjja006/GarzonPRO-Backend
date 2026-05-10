package com.garzonpro.kds.repository;

import com.garzonpro.kds.model.TicketCocina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KdsRepository extends JpaRepository<TicketCocina, Long> {
    // Para ver solo lo que falta por cocinar
    List<TicketCocina> findByEstadoNot(String estado);
}