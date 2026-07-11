package com.garzonpro.Table.controller;

import com.garzonpro.Table.dto.MesaDTO;
import com.garzonpro.Table.model.Mesa;
import com.garzonpro.Table.model.EnumTableStatus;
import com.garzonpro.Table.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/table") // <-- Cambiado de "/tables/mesas" a "/table"
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // Queda en: POST http://localhost:8086/table (o vía Gateway puerto 8080)
    @PostMapping
    public ResponseEntity<Mesa> crear(@Valid @RequestBody MesaDTO dto) {
        Mesa nuevaMesa = mesaService.crearMesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    // Le agregamos "/listar" explícitamente aquí
    // Queda en: GET http://localhost:8080/table/listar
    @GetMapping("/listar") // <-- ¡Aquí le sumamos el mapping que querías!
    public ResponseEntity<List<Mesa>> listar() {
        return ResponseEntity.ok(mesaService.listarTodas());
    }

    // Queda en: PATCH http://localhost:8080/table/{id}/estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Mesa> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EnumTableStatus estado) {
        return ResponseEntity.ok(mesaService.cambiarEstado(id, estado));
    }
}