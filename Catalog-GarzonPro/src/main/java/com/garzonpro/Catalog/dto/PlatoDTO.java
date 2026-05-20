package com.garzonpro.Catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PlatoDTO {
    @NotBlank(message = "El nombre del plato no puede estar vacío")
    private String nombrePlato;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;
}