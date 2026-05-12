package com.garzonpro.Table.service;

import com.garzonpro.Table.dto.MesaDTO;
import com.garzonpro.Table.model.Mesa;
import com.garzonpro.Table.model.EnumTableStatus;
import com.garzonpro.Table.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepo;

    public Mesa crearMesa(MesaDTO dto) {
        Mesa mesa = new Mesa();
        mesa.setNombreMesa(dto.getNombreMesa());
        mesa.setEstado(EnumTableStatus.LIBRE);
        return mesaRepo.save(mesa);
    }

    public List<Mesa> listarTodas() {
        return mesaRepo.findAll();
    }

    public Mesa cambiarEstado(Long idMesa, EnumTableStatus nuevoEstado) {
        Mesa mesa = mesaRepo.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesa.setEstado(nuevoEstado);
        return mesaRepo.save(mesa);
    }
}