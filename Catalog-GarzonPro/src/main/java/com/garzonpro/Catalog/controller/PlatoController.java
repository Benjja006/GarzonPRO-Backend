package com.garzonpro.Catalog.controller;

import com.garzonpro.Catalog.dto.PlatoDTO;
import com.garzonpro.Catalog.model.Plato;
import com.garzonpro.Catalog.service.PlatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/platos")
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    @PostMapping
    public ResponseEntity<Plato> crearPlato(@Valid @RequestBody PlatoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platoService.crearPlato(dto));
    }

    @GetMapping
    public ResponseEntity<List<Plato>> listarTodos() {
        return ResponseEntity.ok(platoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plato> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(platoService.obtenerPorId(id));
    }
}