package com.garzonpro.kds.service;

import com.garzonpro.kds.model.TicketCocina;
import com.garzonpro.kds.repository.KdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KdsService {

    @Autowired
    private KdsRepository kdsRepo;

    public TicketCocina marcarTicketListo(Long idTicket) {
        TicketCocina ticket = kdsRepo.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        ticket.setEstadoGeneral("Listo"); // Cambiamos el estado
        return kdsRepo.save(ticket);      // Persistimos en la DB
    }
}