package com.garzonpro.Catalog.service;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.repository.CategoriaRepository;
import com.garzonpro.Catalog.repository.PlatoRepository;
import com.garzonpro.Catalog.exception.CatalogException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

    private final PlatoRepository platoRepo;
    private final CategoriaRepository categoriaRepo;

    public CatalogService(PlatoRepository platoRepo, CategoriaRepository categoriaRepo) {
        this.platoRepo = platoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    public Plato crearPlato(PlatoDTO dto) {
        log.info("Procesando la creacion de un nuevo plato: {}", dto.getNombrePlato());

        // Regla de Acoplamiento: Validar que la categoría exista antes de insertar
        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> {
                    log.warn("Error de validacion: La categoria con ID {} no existe", dto.getIdCategoria());
                    return new CatalogException("La categoría especificada no existe en el sistema", HttpStatus.BAD_REQUEST);
                });

        Plato plato = new Plato();
        plato.setNombrePlato(dto.getNombrePlato());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria);

        Plato guardado = platoRepo.save(plato);
        log.info("Plato '{}' guardado exitosamente con ID: {}", guardado.getNombrePlato(), guardado.getIdPlato());
        return guardado;
    }

    @Transactional(readOnly = true)
    public List<Plato> obtenerTodosLosPlatos() {
        log.info("Buscando la lista completa de platos del menu");
        return platoRepo.findAll();
    }

    @Transactional
    public Categoria crearCategoria(com.garzonpro.Catalog.dto.CategoriaDTO dto) {
        log.info("Insertando nueva categoria: {}", dto.getNombreCategoria());
        Categoria cat = new Categoria();
        cat.setNombreCategoria(dto.getNombreCategoria());
        return categoriaRepo.save(cat);
    }
}