package com.garzonpro.Kds.controller;

import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import com.garzonpro.Kds.service.KdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kds")
public class KdsController {

    @Autowired
    private KdsService kdsService;

    @Autowired
    private KdsRepository repository;

    // Listar todos los tickets pendientes/preparando
    @GetMapping("/pendientes")
    public List<TicketCocina> obtenerPendientes() {
        // Llamamos al nuevo nombre del método
        return repository.findByEstadoGeneralNot("LISTO");
    }

    // Recibir un nuevo ticket (esto lo llamará el Order-Service después)
    @PostMapping("/nuevo")
    public TicketCocina recibirTicket(@RequestBody TicketCocina ticket) {
        return repository.save(ticket);
    }

    // Marcar como listo
    @PutMapping("/listo/{id}")
    public TicketCocina marcarListo(@PathVariable Long id) {
        return kdsService.marcarTicketListo(id);
    }
}