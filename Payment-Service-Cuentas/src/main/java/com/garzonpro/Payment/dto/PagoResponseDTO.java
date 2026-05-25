package com.garzonpro.Payment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PagoResponseDTO {

    private Long idPago;
    private Long idPedido;
    private Double montoTotal;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;
}