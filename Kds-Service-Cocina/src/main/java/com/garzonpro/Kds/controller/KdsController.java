package com.garzonpro.Kds.controller;

import com.garzonpro.Kds.model.TicketCocina;
import com.garzonpro.Kds.repository.KdsRepository;
import com.garzonpro.Kds.service.KdsService;
import com.garzonpro.Kds.dto.TicketCocinaRequestDTO;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TicketCocina> recibirTicket(@RequestBody TicketCocinaRequestDTO dto) {
        // Transformamos el DTO que viene de Order-Service a una Entidad de KDS
        TicketCocina nuevoTicket = new TicketCocina();
        nuevoTicket.setIdPedido(dto.getIdPedido());
        nuevoTicket.setIdMesa(dto.getIdMesa());
        nuevoTicket.setEstadoGeneral("En Preparación");

        return ResponseEntity.ok(repository.save(nuevoTicket));
    }

    // Marcar como listo
    @PutMapping("/listo/{id}")
    public TicketCocina marcarListo(@PathVariable Long id) {
        return kdsService.marcarTicketListo(id);
    }
}