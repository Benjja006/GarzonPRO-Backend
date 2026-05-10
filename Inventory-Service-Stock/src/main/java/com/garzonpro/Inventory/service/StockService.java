package com.garzonpro.Inventory.service;

import com.garzonpro.Inventory.model.StockPlato;
import com.garzonpro.Inventory.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepo;

    public boolean verificarStock(Long idPlato, Integer cantidadRequerida) {
        return stockRepo.findByIdPlato(idPlato)
                .map(s -> s.getCantidadRestante() >= cantidadRequerida)
                .orElse(false);
    }

    @Transactional
    public void descontarStock(Long idPlato, Integer cantidad) {
        StockPlato stock = stockRepo.findByIdPlato(idPlato)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para plato: " + idPlato));

        if (stock.getCantidadRestante() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        stock.setCantidadRestante(stock.getCantidadRestante() - cantidad);
        stock.verificarDisponibilidad();
        stockRepo.save(stock);
    }

    public StockPlato actualizarStock(Long idPlato, Integer nuevaCantidad) {
        StockPlato stock = stockRepo.findByIdPlato(idPlato)
                .orElse(new StockPlato());

        stock.setIdPlato(idPlato);
        stock.setCantidadRestante(nuevaCantidad);
        stock.verificarDisponibilidad();

        return stockRepo.save(stock);
    }
}