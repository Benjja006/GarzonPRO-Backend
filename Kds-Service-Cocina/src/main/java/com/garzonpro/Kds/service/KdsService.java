package com.garzonpro.Kds.service;

import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
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