package com.garzonpro.Catalog.controller;

import com.garzonpro.Catalog.dto.CategoriaDTO;
import com.garzonpro.Catalog.model.Categoria;
import com.garzonpro.Catalog.service.CategoriaService; // Asegúrate de que tu servicio exista
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/catalog/categorias")
@RequiredArgsConstructor // Genera el constructor para la inyección automática de dependencias
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody CategoriaDTO dto) {
        log.info("API: Recibida petición para crear categoría: {}", dto.getNombreCategoria());
        Categoria nuevaCategoria = categoriaService.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        log.info("API: Recibida petición para listar todas las categorías");
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }
}