package com.garzonpro.Catalog.service;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.repository.PlatoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PlatoService {

    @Autowired
    private PlatoRepository platoRepo;

    public Plato crearPlato(PlatoDTO dto) {
        log.info("Creando nuevo plato en el catálogo: {}", dto.getNombrePlato());

        // Mapeo de DTO a Entidad (como lo pide el estándar)
        Plato plato = new Plato();
        plato.setNombrePlato(dto.getNombrePlato());
        plato.setPrecio(dto.getPrecio());
        plato.setIdCategoria(dto.getIdCategoria());

        return platoRepo.save(plato);
    }

    public List<Plato> listarTodos() {
        log.info("Consultando todos los platos del catálogo");
        return platoRepo.findAll();
    }

    public void actualizarPrecio(Long id, Double nuevoPrecio) {
        log.info("Actualizando precio del plato ID: {}", id);
        Plato plato = platoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        plato.setPrecio(nuevoPrecio);
        platoRepo.save(plato);
    }
}