package com.garzonpro.Catalog.service;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.dto.CategoriaDTO;
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

    // ==========================================
    //          OPERACIONES DE PLATOS
    // ==========================================

    @Transactional
    public Plato crearPlato(PlatoDTO dto) {
        log.info("Validando creacion de plato: {}", dto.getNombrePlato());

        if (platoRepo.existsByNombrePlato(dto.getNombrePlato())) {
            throw new CatalogException("Ya existe un plato con el nombre especificado", HttpStatus.BAD_REQUEST);
        }

        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CatalogException("La categoría especificada no existe", HttpStatus.BAD_REQUEST));

        Plato plato = new Plato();
        plato.setNombrePlato(dto.getNombrePlato());
        plato.setPrecio(dto.getPrecio());
        plato.setCategoria(categoria);

        return platoRepo.save(plato);
    }

    @Transactional
    public Plato modificarPlato(Long id, PlatoDTO dto) {
        log.info("Validando modificacion del plato con ID: {}", id);

        Plato platoExistente = platoRepo.findById(id)
                .orElseThrow(() -> new CatalogException("El plato a modificar no existe", HttpStatus.NOT_FOUND));

        // Si cambia el nombre, validar que el nuevo nombre no esté tomado por otro plato
        if (!platoExistente.getNombrePlato().equalsIgnoreCase(dto.getNombrePlato())
                && platoRepo.existsByNombrePlato(dto.getNombrePlato())) {
            throw new CatalogException("No se puede renombrar; ya existe otro plato con ese nombre", HttpStatus.BAD_REQUEST);
        }

        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CatalogException("La categoría especificada no existe", HttpStatus.BAD_REQUEST));

        platoExistente.setNombrePlato(dto.getNombrePlato());
        platoExistente.setPrecio(dto.getPrecio());
        platoExistente.setCategoria(categoria);

        return platoRepo.save(platoExistente);
    }

    @Transactional
    public void eliminarPlato(Long id) {
        log.info("Eliminando el plato con ID: {}", id);
        if (!platoRepo.existsById(id)) {
            throw new CatalogException("El plato especificado no existe", HttpStatus.NOT_FOUND);
        }
        platoRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Plato> obtenerTodosLosPlatos() {
        return platoRepo.findAll();
    }

    // ==========================================
    //        OPERACIONES DE CATEGORÍAS
    // ==========================================

    @Transactional
    public Categoria crearCategoria(CategoriaDTO dto) {
        log.info("Validando creacion de categoria: {}", dto.getNombreCategoria());

        if (categoriaRepo.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new CatalogException("Ya existe una categoría con el nombre especificado", HttpStatus.BAD_REQUEST);
        }

        Categoria cat = new Categoria();
        cat.setNombreCategoria(dto.getNombreCategoria());
        return categoriaRepo.save(cat);
    }

    @Transactional
    public Categoria modificarCategoria(Long id, CategoriaDTO dto) {
        log.info("Validando modificacion de la categoria con ID: {}", id);

        Categoria catExistente = categoriaRepo.findById(id)
                .orElseThrow(() -> new CatalogException("La categoría a modificar no existe", HttpStatus.NOT_FOUND));

        // Si cambia el nombre, validar que no choque con otra categoría existente
        if (!catExistente.getNombreCategoria().equalsIgnoreCase(dto.getNombreCategoria())
                && categoriaRepo.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new CatalogException("No se puede renombrar; ya existe otra categoría con ese nombre", HttpStatus.BAD_REQUEST);
        }

        catExistente.setNombreCategoria(dto.getNombreCategoria());
        return categoriaRepo.save(catExistente);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        log.info("Validando eliminacion de la categoria con ID: {}", id);

        if (!categoriaRepo.existsById(id)) {
            throw new CatalogException("La categoría especificada no existe", HttpStatus.NOT_FOUND);
        }

        // Regla de integridad: No borrar si tiene platos asignados
        if (platoRepo.existsByCategoriaIdCategoria(id)) {
            throw new CatalogException("No se puede eliminar la categoría porque contiene platos asociados", HttpStatus.CONFLICT);
        }

        categoriaRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Categoria> obtenerCategorias() {
        return categoriaRepo.findAll();
    }
}