package com.garzonpro.Catalog.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDTO {
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombreCategoria;

    public CategoriaDTO() {}
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}