package com.garzonpro.Inventory.controller;

import com.garzonpro.Inventory.model.StockPlato;
import com.garzonpro.Inventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/check/{idPlato}")
    public boolean check(@PathVariable Long idPlato, @RequestParam Integer cantidad) {
        return stockService.verificarStock(idPlato, cantidad);
    }

    @PostMapping("/update")
    public StockPlato update(@RequestParam Long idPlato, @RequestParam Integer cantidad) {
        return stockService.actualizarStock(idPlato, cantidad);
    }

    @PostMapping("/reduce/{idPlato}")
    public void reduce(@PathVariable Long idPlato, @RequestParam Integer cantidad) {
        stockService.descontarStock(idPlato, cantidad);
    }
}