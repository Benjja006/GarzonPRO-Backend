package com.garzonpro.Inventory.controller;

import com.garzonpro.Inventory.dto.StockDTO;
import com.garzonpro.Inventory.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    // Para cargar stock inicial de un plato
    @PostMapping("/inicializar")
    public ResponseEntity<String> inicializar(@Valid @RequestBody StockDTO dto) {
        stockService.inicializarStock(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Stock inicializado correctamente");
    }

    // Para verificar si hay stock (útil para el Order-Service después)
    @GetMapping("/verificar/{idPlato}/{cantidad}")
    public ResponseEntity<Boolean> verificar(@PathVariable Long idPlato, @PathVariable Integer cantidad) {
        boolean hayStock = stockService.verificarStock(idPlato, cantidad);
        return ResponseEntity.ok(hayStock);
    }

    // Endpoint manual para descontar (en el futuro lo hará el Order-Service)
    @PostMapping("/descontar")
    public ResponseEntity<String> descontar(@Valid @RequestBody com.garzonpro.Inventory.dto.DescontarStockDTO dto) {

        // 1. Verificamos si hay stock suficiente antes de descontar
        boolean hayStock = stockService.verificarStock(dto.getIdPlato(), dto.getCantidad());
        if (!hayStock) {
            throw new IllegalArgumentException("No hay stock suficiente para el plato ID: " + dto.getIdPlato());
        }

        // 2. Procedemos a descontar
        stockService.descontarStock(dto.getIdPlato(), dto.getCantidad());

        return ResponseEntity.ok("Stock actualizado con éxito para el plato ID: " + dto.getIdPlato());
    }
}