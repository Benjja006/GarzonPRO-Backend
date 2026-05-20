package com.garzonpro.Catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio y no puede estar vacío")
    private String nombreCategoria;
}