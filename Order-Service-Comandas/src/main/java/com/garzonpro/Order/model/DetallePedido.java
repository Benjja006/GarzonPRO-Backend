package com.garzonpro.Order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    // Relación inversa hacia Pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    private Long idPlato;
    private String nombrePlato;
    private Integer cantidad;
    private BigDecimal precioUnitarioAlMomentoVenta;
}