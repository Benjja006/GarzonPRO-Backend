package com.garzonpro.Catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PlatoDTO {

    @NotBlank(message = "El nombre del plato no puede estar vacío")
    private String nombrePlato;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número positivo")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;

    public PlatoDTO() {}

    // === GETTERS Y SETTERS MANUALES ===
    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
}