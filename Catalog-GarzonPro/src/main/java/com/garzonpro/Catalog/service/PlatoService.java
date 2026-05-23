package com.garzonpro.Catalog.service;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.repository.CategoriaRepository;
import com.garzonpro.Catalog.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoService {

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Plato crearPlato(PlatoDTO dto) {
        // Buscamos la categoría primero
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + dto.getIdCategoria()));

        Plato plato = new Plato();
        plato.setNombrePlato(dto.getNombrePlato());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria); // Asignamos el objeto, no el número plano

        return platoRepository.save(plato);
    }

    public List<Plato> listarTodos() {
        return platoRepository.findAll();
    }

    public Plato obtenerPorId(Long id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con el ID: " + id));
    }
}