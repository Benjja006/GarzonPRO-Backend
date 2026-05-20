package com.garzonpro.Inventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stock_plato")
@Data
public class StockPlato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    private Long idPlato; // Relación lógica con el Catalog-Service
    private Boolean disponibleParaVenta;
    private Integer cantidadRestante;
}