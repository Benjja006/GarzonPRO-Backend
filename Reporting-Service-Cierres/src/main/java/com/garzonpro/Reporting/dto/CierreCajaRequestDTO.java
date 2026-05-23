package com.garzonpro.Reporting.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CierreCajaRequestDTO {

    @NotNull(message = "El total de ventas del día es obligatorio")
    @PositiveOrZero(message = "El total de ventas no puede ser negativo")
    private Double totalVentasDia;

    @NotNull(message = "La cantidad de pedidos es obligatoria")
    @PositiveOrZero(message = "La cantidad de pedidos no puede ser negativa")
    private Integer cantidadPedidos;
}