package com.garzonpro.Inventory.repository;

import com.garzonpro.Inventory.model.StockPlato;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<StockPlato, Long> {
    Optional<StockPlato> findByIdPlato(Long idPlato);
}