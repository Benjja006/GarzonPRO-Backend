package com.garzonpro.Order.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_pedido")
@Data
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private Long idPlato;
    private String nombrePlato;
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitarioAlMomentoVenta;

    // NUEVO CAMPO: Soporte para notas personalizadas enviadas por el Garzón
    @Column(length = 255)
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
}