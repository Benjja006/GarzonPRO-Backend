package com.garzonpro.Catalog.controller;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/catalogo/platos")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Plato> crearPlato(@Valid @RequestBody PlatoDTO dto) {
        Plato nuevoPlato = service.crearPlato(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPlato);
    }

    @GetMapping
    public ResponseEntity<List<Plato>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodosLosPlatos());
    }

    @PostMapping("/categorias")
    public ResponseEntity<com.garzonpro.Catalog.model.Categoria> crearCategoria(@Valid @RequestBody com.garzonpro.Catalog.dto.CategoriaDTO dto) {
        com.garzonpro.Catalog.model.Categoria nueva = service.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
}