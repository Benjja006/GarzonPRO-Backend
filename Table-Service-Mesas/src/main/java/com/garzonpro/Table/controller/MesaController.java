package com.garzonpro.Table.controller;

import com.garzonpro.Table.dto.MesaDTO;
import com.garzonpro.Table.model.EnumTableStatus;
import com.garzonpro.Table.model.Mesa;
import com.garzonpro.Table.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @PostMapping
    public ResponseEntity<Mesa> crear(@Valid @RequestBody MesaDTO dto) {
        // CORRECCIÓN CRÍTICA: Se repara la asignación de variable rota en el PDF original
        Mesa nuevaMesa = mesaService.crearMesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> listar() {
        return ResponseEntity.ok(mesaService.listarTodas());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Mesa> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EnumTableStatus estado) {
        return ResponseEntity.ok(mesaService.cambiarEstado(id, estado));
    }
}