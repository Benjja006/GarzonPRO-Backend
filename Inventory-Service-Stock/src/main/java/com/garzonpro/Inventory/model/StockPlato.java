package com.garzonpro.Inventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stock_platos")
@Data
public class StockPlato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    private Long idPlato; // Relación lógica con el Catalog Service

    private Integer cantidadRestante;

    private Boolean disponibleParaVenta;

    // Método para actualizar disponibilidad automáticamente
    public void verificarDisponibilidad() {
        this.disponibleParaVenta = (this.cantidadRestante > 0);
    }
}