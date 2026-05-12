package com.garzonpro.Payment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private Long idPedido; // Referencia al pedido que se está pagando
    private Double montoTotal;
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA
    private String estadoPago = "COMPLETADO";
    private LocalDateTime fechaPago = LocalDateTime.now();
}