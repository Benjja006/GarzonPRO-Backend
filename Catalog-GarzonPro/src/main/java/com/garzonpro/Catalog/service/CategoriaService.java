package com.garzonpro.Catalog.service;

import com.garzonpro.Catalog.dto.CategoriaDTO;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.repository.CategoriaRepository; // Tu interfaz JpaRepository
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;

    public Categoria crearCategoria(CategoriaDTO dto) {
        log.info("Guardando nueva categoría: {}", dto.getNombreCategoria());

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(dto.getNombreCategoria());

        return categoriaRepo.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepo.findAll();
    }
}