package com.garzonpro.Inventory.service;

import com.garzonpro.Inventory.dto.StockDTO;
import com.garzonpro.Inventory.model.StockPlato;
import com.garzonpro.Inventory.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Slf4j
@Service
public class StockService {

    @Autowired
    private StockRepository stockRepo;

    public void inicializarStock(StockDTO dto) {
        StockPlato stock = new StockPlato();
        stock.setIdPlato(dto.getIdPlato());
        stock.setCantidadRestante(dto.getCantidad());
        stock.setDisponibleParaVenta(dto.getCantidad() > 0);
        stockRepo.save(stock);
    }

    // Método solicitado en tu UML: verificarStock()
    public boolean verificarStock(Long idPlato, Integer cantidadPedida) {
        return stockRepo.findByIdPlato(idPlato)
                .map(s -> s.getCantidadRestante() >= cantidadPedida)
                .orElse(false);
    }

    // Método solicitado en tu UML: descontarStock()
    @Transactional
    public void descontarStock(Long idPlato, Integer cantidadADescontar) {
        StockPlato stock = stockRepo.findByIdPlato(idPlato)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para el plato: " + idPlato));

        if (stock.getCantidadRestante() < cantidadADescontar) {
            throw new RuntimeException("Stock insuficiente para el plato: " + idPlato);
        }

        stock.setCantidadRestante(stock.getCantidadRestante() - cantidadADescontar);

        // Si llega a cero, lo marcamos como no disponible
        if (stock.getCantidadRestante() == 0) {
            stock.setDisponibleParaVenta(false);
        }

        stockRepo.save(stock);
        log.info("Stock descontado. Nuevo saldo para plato {}: {}", idPlato, stock.getCantidadRestante());
    }
}