package com.garzonpro.Order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private Long idMesa;
    private String estadoGeneral = "ABIERTO"; // ABIERTO, PAGADO, CANCELADO
    private Double totalParcial = 0.0;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public void agregarDetalle(DetallePedido detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
        detalle.setPedido(this);
    }

    public void recalcularTotal() {
        this.totalParcial = detalles.stream()
                .mapToDouble(d -> d.getPrecioUnitarioAlMomentoVenta() * d.getCantidad())
                .sum();
    }
}