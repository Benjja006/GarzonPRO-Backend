package com.garzonpro.Inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DescontarStockDTO {

    @NotNull(message = "El ID del plato es obligatorio")
    @Positive(message = "El ID del plato debe ser válido")
    private Long idPlato;

    @NotNull(message = "La cantidad a descontar es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}