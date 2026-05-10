package com.garzonpro.table.service;

import com.garzonpro.table.model.Mesa;
import com.garzonpro.table.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepo;

    // Métodos definidos en tu UML
    public Mesa abrirMesa(Long id) {
        Mesa mesa = mesaRepo.findById(id).orElseThrow();
        mesa.setEstado("Ocupada"); // Según EnumTableStatus
        return mesaRepo.save(mesa);
    }

    public Mesa cerrarMesa(Long id) {
        Mesa mesa = mesaRepo.findById(id).orElseThrow();
        mesa.setEstado("Libre"); // Según EnumTableStatus
        return mesaRepo.save(mesa);
    }

    public Mesa cambiarEstado(Long id, String nuevoEstado) {
        Mesa mesa = mesaRepo.findById(id).orElseThrow();
        mesa.setEstado(nuevoEstado);
        return mesaRepo.save(mesa);
    }
}