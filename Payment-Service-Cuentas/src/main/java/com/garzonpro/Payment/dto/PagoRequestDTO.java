package com.garzonpro.Payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser un número positivo")
    private Long idPedido;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser mayor a 0")
    private Double montoTotal;

    @NotNull(message = "El método de pago es obligatorio")
    @Pattern(
            regexp = "EFECTIVO|TARJETA|TRANSFERENCIA",
            message = "El método de pago debe ser: EFECTIVO, TARJETA o TRANSFERENCIA"
    )
    private String metodoPago;
}